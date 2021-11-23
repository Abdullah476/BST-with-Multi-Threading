import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class VocabFileReadingThread extends Thread {
	private String filePath;
	
	VocabFileReadingThread(int filePath){
		this.filePath = MainClass.fileNames.get(filePath);
	}
	
	public void run() {
		
		String temp = "";
		
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
						
						//Adding all the words in vocabulary BST
						MainClass.vocabTree.insert(temp);
						
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
		
	}
}
