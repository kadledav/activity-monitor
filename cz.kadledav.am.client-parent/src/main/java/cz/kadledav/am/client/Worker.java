package cz.kadledav.am.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class Worker {
	
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);

	private final DynamicIntProperty requestTimeout = DynamicPropertyFactory.getInstance().getIntProperty("request.timeout", 5000);
	private final DynamicStringProperty requestUri = DynamicPropertyFactory.getInstance().getStringProperty("request.uri", "http://localhost");
	private final DynamicStringProperty requestUser = DynamicPropertyFactory.getInstance().getStringProperty("request.user", "admin");
	private final DynamicStringProperty requestPassword = DynamicPropertyFactory.getInstance().getStringProperty("request.password", "admin");

	private final DynamicIntProperty repeatInSec = DynamicPropertyFactory.getInstance().getIntProperty("worker.sleep.sec", 30);
	
	
	 private final LinkedList<StateInfo> stateQueue = new LinkedList<>();

	public void start() {
		while (Thread.interrupted() == false){
			try {
				runJob();
				Thread.sleep(Duration.ofSeconds(repeatInSec.get()).toMillis());
			} catch (InterruptedException eInt){
				logger.info("Thread interupted - exiting");
				return;
			} catch (Exception e) {
				logger.warn("unexpected exception during process");
			} 
		}
	}


	private void runJob() {
		URI uri = createUri();
		if(uri == null){
			return;
		}
		StateInfo info = StateInformatorFactory.getInformator().getInfo();
		try {
			sendInfo(uri, info);
		} catch (IOException e) {
			logger.warn("I/O exception during post to {}",uri, e);
			stateQueue.addLast(info);
			return;
		} 
		emtpyQueue(uri);
	}


	private void emtpyQueue(URI uri) {
		StateInfo peek; 
		while ((peek = stateQueue.peek()) != null){
			try {
				sendInfo(uri, peek);
				stateQueue.remove(peek);
			} catch (IOException e) {
				logger.debug("Unable emtpy queue");
				return;
			}
		}
	}


	private void sendInfo(URI uri, StateInfo info) throws ClientProtocolException, IOException {
		String jsonContent = info.getProcess();
		Request.Post(uri).connectTimeout(requestTimeout.get()).socketTimeout(requestTimeout.get()).bodyString(jsonContent, ContentType.APPLICATION_JSON).execute().returnContent();
	}


	private URI createUri() {
		URI uri = null;
		try {
			uri = new URIBuilder(requestUri.get()).addParameter("user", requestUser.get()).addParameter("password", requestPassword.get()).build();
		} catch (URISyntaxException e) {
			logger.warn("Cannot build URI", e);
		}
		return uri;
	}
}
