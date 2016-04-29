package com.cmz.web.util;

import java.io.IOException;
import java.util.List;

public interface IFile {
	/**
	 * 写文件
	 * 
	 * @param fileName
	 * @param content
	 */
	public void write(String fileName, byte[] content) throws IOException;

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public List<String> read(String fileName) throws IOException;

	/**
	 * 重命名、移动文件 从from 到 to
	 * 
	 * @param from1
	 * @param to1
	 * @throws IOException 
	 */
	public void move(String from1, String to1) throws IOException;
	/**
	 * 复制文件
	 * @param from1
	 * @param to1
	 * @throws IOException 
	 */
	public void copy(String from1, String to1) throws IOException;
	/**
	 * 文件比较
	 * @param file1
	 * @param file2
	 * @throws IOException
	 */
	public boolean compare(String fileName1,String fileName2) throws IOException;
}
