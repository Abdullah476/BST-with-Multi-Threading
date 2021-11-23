import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

public class InputFileReadingThread extends Thread{
	private String filePath;
	private Vector<String> tempVector = new Vector<>();
	
	InputFileReadingThread(int filePath){
		this.filePath = MainClass.fileNames.get(filePath);
	}
	
	public void run() {
		
		String temp = "";
		tempVector.add(filePath);
		
		try {
			RandomAccessFile file = new RandomAccessFile(filePath, "r");
			FileChannel channel = file.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while(channel.read(buffer) > 0) {
				buffer.flip();
				for(int i = 0; i < buffer.limit(); i++) {
					char tempO = (char)buffer.get();
					if(tempO != '\n') {
						temp += tempO;
					}
					else {
						temp = temp.replace("\r\n", "").replace("\r", "");
						tempVector.add(temp);						
						temp = "";
					}
				}
				buffer.clear();
			}
					
			//Closing the file and channel
			channel.close();
			file.close();
		}catch(IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		//Adding tempVector to the MainVector
		MainClass.inputFiles.add(tempVector);
			
	}
	
}
