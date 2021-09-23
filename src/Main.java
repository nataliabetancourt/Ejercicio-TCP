import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import model.Message;

public class Main extends PApplet{

	private ServerSocket ss;
	private Socket connection;
	private BufferedWriter bw;
	private BufferedReader bf;
	private int x, y, color;
	private Message message;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	@Override
	public void settings() {
		size(700, 500);
	}
	
	@Override
	public void setup() {
		//Ball coordinates to start with
		x = 350;
		y = 250;
		color = color (0);
		
		new Thread(
				()->{
					try {
						System.out.println("Esperando");
						ss = new ServerSocket(7000);
						connection = ss.accept();
						System.out.println("Conectado");
						
						//To receive
						InputStream is = connection.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						bf = new BufferedReader(isr);
						
						//To transmit
						OutputStream os = connection.getOutputStream();
						OutputStreamWriter osw = new OutputStreamWriter(os);
						bw = new BufferedWriter(osw);
						
						receiveMessage();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();

	}
	
	@Override
	public void draw() {
		background(255);
		noStroke();
		fill(color);
		ellipse(x, y, 80, 80);
		
		
	}
	
	public void receiveMessage() {
		new Thread(
				() -> {
					while(true) {
						try {
							String msg = bf.readLine(); //Stops program until there is a message
							System.out.println("Mensaje recibido: " + msg);

							//Turn message into object
							Gson gson = new Gson();
							message = gson.fromJson(msg, Message.class);
							//Change the ball based on object received
							ballChanges();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();	
	}
	
	private void ballChanges() {
			switch (message.getChange()) {
			case "up":
					y -= message.getMovementValue();
				break;
			case "down":
					y += message.getMovementValue();
				break;
			case "left":
					x -= message.getMovementValue();
				break;
			case "right":
					x += message.getMovementValue();
				break;
			case "color":
					color = color (random(255),random(255),random(255));
				break;
		}

	}

	public void sendMessage(String message) {
		//Write message
		try {
			bw.write(message);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
}
