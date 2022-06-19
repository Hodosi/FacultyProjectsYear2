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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SkeletonServicesRpcProxy implements ISkeletonServices {
    private String host;
    private int port;

    private ISkeletonObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public SkeletonServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(User user, ISkeletonObserver client) throws SkeletonException {
        System.out.println("PROXY - LOGIN");
        initializeConnection();
        Request req=new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new SkeletonException(err);
        }
    }

    @Override
    public void logout(User user, ISkeletonObserver client) throws SkeletonException {
        System.out.println("PROXY - LOGOUT");
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new SkeletonException(err);
        }
    }

    @Override
    public User findUserByUsername(String username) throws SkeletonException {
        System.out.println("PROXY - FIND BY USERNAME");
        User userDTO = new User(username, "test");
        Request req=new Request.Builder().type(RequestType.FIND_USER_BY_USERNAME).data(userDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new SkeletonException(err);
        }
        User user = (User) response.data();
        return user;
    }

    @Override
    public void startGame(User user, String startGameData) throws SkeletonException {
        System.out.println("PROXY - START GAME");
        StartGameDTO startGameDTO = new StartGameDTO(user, startGameData);
        Request req=new Request.Builder().type(RequestType.START_GAME).data(startGameDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new SkeletonException(err);
        }
    }

    @Override
    public Move[] findCurrentMoves() throws SkeletonException {
        System.out.println("PROXY - FIND ALL TEST DTOs");
        Request req=new Request.Builder().type(RequestType.FIND_CURRENT_MOVE).data(null).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new SkeletonException(err);
        }
        Move[] moves =(Move[])response.data();
        return moves;
    }

    //--------------------------------------------------------------------------------------------------------------
    private void closeConnection() {
        System.out.println("PROXY - CLOSE CONNECTION");
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void sendRequest(Request request) throws SkeletonException {
        System.out.println("PROXY - SEND REQUEST");
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new SkeletonException("Error sending object "+ e);
        }
    }

    private Response readResponse() throws SkeletonException {
        System.out.println("PROXY - READ RESPONSE");
        Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("PROXY - RESPONSE READ");
        return response;
    }

    private void initializeConnection() throws SkeletonException {
        System.out.println("PROXY - INITIALIZE CONNECTION");
        try{
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        }catch (IOException e){
            System.out.println("ERROR - INITIALIZE CONNECTION");
            e.printStackTrace();
        }
    }

    private void startReader(){
        System.out.println("PROXY - START READER");

        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        System.out.println("PROXY - NEW MOVE");
        if(response.type() == ResponseType.NEW_MOVE){
            System.out.println("new participant ");
            try{
                client.newMove();
            }catch (SkeletonException exception){
                exception.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        System.out.println("PROXY - IS UPDATE");
        return response.type()== ResponseType.NEW_MOVE;
    }


    private class ReaderThread implements Runnable{
        public void run() {
            System.out.println("PROXY - READER THREAD");
            while(!finished){
                try {
                    System.out.println("try to read response");
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("IO");
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Class");
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
