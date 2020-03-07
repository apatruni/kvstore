package patruni.kvstore;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConnectionEstablishJob {
	
	@Autowired
	private ConnectionEstablisher ce;
	
	private boolean stop=false;
	
	@Scheduled(fixedDelay=10000)
	private void tryEstablish() {
		System.out.println(Thread.currentThread() + " here3");
		if (stop) return;
		boolean done = false;
		try {
			done = ce.establish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (done) stop=true;
	}
}
