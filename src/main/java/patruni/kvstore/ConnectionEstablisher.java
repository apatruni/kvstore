package patruni.kvstore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConnectionEstablisher {
	
	@Value("${server.list}")
	List<String> serverLis;
	
	@Value("${server.curr}")
	String serverCurr;
	
	@Value("${port}")
	Integer port;
	
	private List<Socket> lisConnections = new ArrayList<>();
	
	@PostConstruct
	private void setupServer() throws IOException {
		System.out.println("here1" + Thread.currentThread());
		Thread c = new C();
		c.start();
		
	}

	public boolean establish() throws IOException {
		System.out.println("here2");
		List<String> others = new ArrayList<String>(serverLis);
		others.remove(serverCurr);
		int j=0;
		List<Socket> clis = new ArrayList<>();
		for(int i=0;i<others.size();i++) {
			try {
				Integer port = Integer.parseInt(others.get(i).split(":")[1]);
				Socket cli = new Socket();
				System.out.println("connecting to: " + port);
				cli.connect(new InetSocketAddress(others.get(i).split(":")[0],port));
				clis.add(cli);
				cli.getOutputStream().write(("hello from " + this.port + " " + cli.getLocalPort()).getBytes());
				j++;
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
		if (j==others.size()) {
			System.out.println("Connected!");
			return true;
			
		} else {
			for(int i=0;i<clis.size();i++) {
				clis.get(i).close();
			}
			return false;
		}
	}
	
	class C extends Thread {
		public void run() {
			ServerSocket s;
			try {
				System.out.println("in listener ");
				s = new ServerSocket(port);
				byte[] b = new byte[1000];
				while(true) {
					Socket cli = s.accept();
					int k = cli.getInputStream().read(b);
					System.out.println("123213123 " + new String(b,0,k));
					lisConnections.add(cli);
					System.out.println("accepted: " + cli);
					cli.getOutputStream().write(("hello again from " + port + " ").getBytes());
				}
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}


}
