package skeleton.network.rpcprotocol;

import skeleton.model.Move;
import skeleton.model.StartGameDTO;
import skeleton.model.User;
import skeleton.services.ISkeletonObserver;
import skeleton.services.ISkeletonServices;
import skeleton.services.SkeletonException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SkeletonClientRpcWorker implements Runnable, ISkeletonObserver {
    private ISkeletonServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public SkeletonClientRpcWorker(ISkeletonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        System.out.println("WORKER - RUN");
        while(connected){
            try {
                System.out.println("read request");
                Object request=input.readObject();
                System.out.println((Request)request);
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    @Override
    public void newMove() throws SkeletonException {
        System.out.println("WORKER - NEW MOVE");
        Response resp=new Response.Builder().type(ResponseType.NEW_MOVE).data(null).build();
        System.out.println("Participant saved");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new SkeletonException("Sending error: "+e);
        }

    }


    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        System.out.println("WORKER - HANDLE REQUEST");
        Response response=null;

        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            User user= (User)request.data();
            try {
                server.login(user, this);
                return okResponse;

            }
            catch (SkeletonException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            User user= (User)request.data();
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            }
            catch (SkeletonException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type() == RequestType.FIND_USER_BY_USERNAME){
            System.out.println("Find by user by username request");
            User userDTO = (User) request.data();
            try{
                User user = server.findUserByUsername(userDTO.getUsername());
                return new Response.Builder().type(ResponseType.FIND_USER_BY_USERNAME).data(user).build();

            }
            catch (SkeletonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type() == RequestType.START_GAME){
            System.out.println("Start Game request");
            StartGameDTO startGameDTO = (StartGameDTO) request.data();
            try{
                server.startGame(startGameDTO.getUser(), startGameDTO.getStartGameData());
                return okResponse;
            }
            catch (SkeletonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.FIND_CURRENT_MOVE){
            System.out.println("Find all test dto request");
            try{
                Move[] moves = server.findCurrentMoves();
                return new Response.Builder().type(ResponseType.FIND_CURRENT_MOVE).data(moves).build();

            }
            catch (SkeletonException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("WORKER - SEND RESPONSE");
        System.out.println("sending response "+response);
        synchronized (output){
            output.writeObject(response);
            output.flush();
        }
    }
}
