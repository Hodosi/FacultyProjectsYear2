package skeleton.network.utils;

import skeleton.network.rpcprotocol.SkeletonClientRpcWorker;
import skeleton.services.ISkeletonServices;

import java.net.Socket;

public class SkeletonRpcConcurrentServer extends AbstractConcurrentServer {
    private ISkeletonServices competitionServer;
    public SkeletonRpcConcurrentServer(int port, ISkeletonServices competitionServer) {
        super(port);
        this.competitionServer = competitionServer;
        System.out.println("Competition - CompetitionRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        SkeletonClientRpcWorker worker=new SkeletonClientRpcWorker(competitionServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
