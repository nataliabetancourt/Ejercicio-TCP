import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import processing.core.PApplet;

public class MainServer extends PApplet{
	
	private BufferedWriter bw;
	private BufferedReader bf;
	private int bx, by;

	public static void main(String[] args) {
		PApplet.main("MainServer");
	}
		
	
	@Override
	public void settings() {
		size(500, 500);
	}
	
	@Override
	public void setup() {
		bx = 250;
		by = 250;
		
		new Thread(
				()->{
					try {
						System.out.println("Iniciando servidor");
						ServerSocket ss = new ServerSocket(9000);
						System.out.println("Esperando clientes");
						Socket connection = ss.accept(); //Stops program until there is a connection
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}).start();
	}
	
	@Override
	public void draw() {
		background(169, 245, 255);
		textAlign(CENTER);
		textSize(18);
		text("Server", 250, 30);
		
		fill(255, 255, 255);
		ellipse(bx, by, 50, 50);
		
	}
	
	
	public void receiveMessage() {
		new Thread(
				() -> {
					while(true) {
						try {
							System.out.println("Esperando mensaje del cliente");
							String msg = bf.readLine(); //Stops program until there is a message
							System.out.println("Mensaje recibido: " + msg);
							String [] coord = msg.split(":");
							
							//Coordinates
							bx = Integer.parseInt(coord[0]);
							by = Integer.parseInt(coord[1]);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();	
	}

	public void sendMessage(String message) {
		//Write message
		try {
			bw.write(message);
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
