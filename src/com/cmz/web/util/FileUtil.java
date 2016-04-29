package com.cmz.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.commons.codec.Charsets;

import com.google.common.io.Files;

/**
 * @author mingzhou.chen 28458942@qq.com 2015年12月25日 下午4:55:49
 */
public final class FileUtil implements IFile {
	
	/**
	 * 写文件
	 * @param fileName
	 * @param content
	 * @param op  1 新文件  2 追加
	 * @throws IOException 
	 */
	public void write(String fileName, byte[] content) throws IOException {
		final File newFile = new File(fileName);
		Files.write(content, newFile);
	}

	public void append(String fileName,String content) throws IOException{
		final File newFile = new File(fileName);
		Files.append(content, newFile, Charsets.UTF_8);
	}
/*	
	 public  void append(String fileName, String content) {
	        try {
	            // 打开一个随机访问文件流，按读写方式
	            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
	            // 文件长度，字节数
	            long fileLength = randomFile.length();
	            //将写文件指针移到文件尾。
	            randomFile.seek(fileLength);
	            randomFile.writeBytes(content);
	            randomFile.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void append1(String fileName, char[] content) {
	        try {
	            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	            FileWriter writer = new FileWriter(fileName, true);
	            writer.write(content);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    public  void append2(String fileName, byte[] content) {
	        try {
	            // 打开一个随机访问文件流，按读写方式
	            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
	            long fileLength = randomFile.length();
	            randomFile.close();
	            
	            FileOutputStream fos = new FileOutputStream(fileName);
		          fos.write(content, (int)(fileLength), (int)content.length);
		          fos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	*/
	public List<String> read(String fileName) throws IOException {
		File testFile = new File(fileName);
		List<String> lines = Files.readLines(testFile, null);
		for (String line : lines) {
			System.out.println(line);
		}
		return lines;
	}

	public void move(String from1, String to1) throws IOException {
		File from = new File(from1);
		File to = new File(to1);
		Files.move(from, to);
	}
	public void delete(String fileName) throws IOException{
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
		}
	}
	public void copy(String from1, String to1) throws IOException{
		File from = new File(from1);
		File to = new File(to1);
		Files.copy(from, to);
	}
	
	@Override
	public boolean compare(String fileName1, String fileName2) throws IOException {
		File file1 = new File(fileName1);
		File file2 = new File(fileName2);
		return Files.equal(file1, file2);
	}
	
	public static void main(String[] args) throws IOException {
		FileUtil fileUtil = new FileUtil();
		fileUtil.append("file.txt", "你好");
	}
}
