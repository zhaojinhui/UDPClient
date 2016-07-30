import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	public static void main(String[] args) {
		try {
			DatagramSocket socket=new DatagramSocket();
			try{
				System.out.println("input exit is quit, please input ip");
				
				//get the server ip
				String strip;
				BufferedReader inputBufferedip=new BufferedReader(new InputStreamReader(System.in));
				strip=inputBufferedip.readLine();
				InetAddress serverip=InetAddress.getByName(strip);
				System.out.println("input port");
				
				//get the server port
				String serverport;
				BufferedReader inputBufferedport=new BufferedReader(new InputStreamReader(System.in));
				serverport=inputBufferedport.readLine();
				
				//send greeting message
				String greetingString="greeting";
				byte[] sendbuf=greetingString.getBytes();
				DatagramPacket sendpacket= new DatagramPacket(sendbuf, sendbuf.length,serverip,Integer.parseInt(serverport));
				socket.send(sendpacket);
				
				//waiting for response
				byte[] getbuf=new byte [2048];
				DatagramPacket getPacket=new DatagramPacket(getbuf, getbuf.length);
				socket.receive(getPacket);
				if(getPacket.getLength()!=0)
				{
					System.out.println("connected");
					
					//send message thread
					Runnable runnable=new sendhandler(socket,serverip,serverport);
					Thread sendtThread=new Thread(runnable);
					
					//receive message thread
					Runnable reRunnable=new receivehandler(socket);
					Thread receviveThread=new Thread(reRunnable);
					
					//start thread until the send thread dead
					sendtThread.start();
					receviveThread.start();
					while(sendtThread.isAlive())
					{
						
					}
				}
			}finally{
				socket.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

//send thread
class sendhandler implements Runnable{
	DatagramSocket socket;
	InetAddress ip;
	int port;
	public sendhandler(DatagramSocket i, InetAddress serverip, String serverport) {
		// initial variable
		socket=i;
		ip=serverip;
		port=Integer.parseInt(serverport);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				
				//send message
				String message;
				BufferedReader inputBufferedReader=new BufferedReader(new InputStreamReader(System.in));
				message=inputBufferedReader.readLine();
				if(message.equals("exit"))
				{
					break;
				}
				byte[] sendmessage=message.getBytes();
				DatagramPacket sendms=new DatagramPacket(sendmessage,sendmessage.length,ip,port);
				socket.send(sendms);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class receivehandler implements Runnable{
	DatagramSocket socket;
	public receivehandler(DatagramSocket i) {
		// initial varibale
		socket=i;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				
				//receive message
				byte[] getmessage=new byte [2048];
				DatagramPacket getms=new DatagramPacket(getmessage,getmessage.length);
				socket.receive(getms);
				String recmessage=new String(getmessage,0,getms.getLength());
				System.out.println(recmessage);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}





