package com.yxh.ryt.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Title: Nick Lious
 * 
 * Description: Nick Lious
 * 
 * Copyright: Copyright (c) 2011
 * 
 * Organization: God
 * 
 * @author Nick Lious
 * @version 1.0
 */
public class FileUtil {

	public static boolean createNewFile(File f) {
		if (null == f) {
			return false;
		}
		makesureParentExist(f);
		if (f.exists()) {
			delete(f);
		}
		try {
			return f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void delete(File file) {
		if (null == file || !file.exists())
			return;
		file.delete();
	}

	public static void delete(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		File f = new File(filepath);
		delete(f);
	}

	public static void deleteFiles(File file) {
		if (null == file || !file.exists())
			return;
		if (file.isFile()) {
			file.delete();
		} else {
			for (File f : file.listFiles()) {
				delete(f);
			}
			file.delete();
		}
	}

	public static void deleteFiles(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		File f = new File(filepath);
		deleteFiles(f);
	}

	public static void makesureParentExist(File file) {
		if (null == file) {
			return;
		}
		File pf = file.getParentFile();
		if (pf == null || pf.exists()) {
			return;
		}
		mkdirs(pf);
	}

	public static void makesureParentExist(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		makesureParentExist(new File(filepath));
	}

	public static void makesureDirExist(File f) {
		if (null == f) {
			return;
		}
		if (f.exists()) {
			if (f.isDirectory()) {
				return;
			} else if (f.isFile()) {
				delete(f);
			}
		}
		makesureParentExist(f);
		f.mkdir();
	}

	public static void makesureDirExist(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		makesureDirExist(new File(filepath));
	}

	public static void mkdirs(File file) {
		if (null == file || file.exists()) {
			return;
		}
		if (file.mkdirs()) {
			return;
		}
	}

	public static void mkdirs(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		mkdirs(new File(filepath));
	}

	public static FileInputStream getFileInputStream(File f) {
		if (null == f) {
			return null;
		}
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FileInputStream getFileInputStream(String filepath) {
		return getFileInputStream(new File(filepath));
	}

	public static FileOutputStream getFileOutputStream(File f) {
		try {
			return new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FileOutputStream getFileOutputStream(String filepath) {
		return getFileOutputStream(new File(filepath));
	}

	public static FileOutputStream getEmptyFileOutputStream(File f) {
		if (null == f) {
			return null;
		}
		if (!f.exists()) {
			makesureParentExist(f);
		}
		createNewFile(f);
		try {
			return new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FileOutputStream getEmptyFileOutputStream(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return null;
		}
		return getEmptyFileOutputStream(new File(filepath));
	}

	public static void writeToRandomAccessFile(RandomAccessFile f, InputStream ins) {
		if (null == f || null == ins) {
			return;
		}
		BufferedInputStream bis = null;
		if (ins instanceof BufferedInputStream) {
			bis = (BufferedInputStream) ins;
		} else {
			bis = new BufferedInputStream(ins);
		}
		byte[] buffer = new byte[512];
		try {
			int bytesRead = 0;
			while ((bytesRead = bis.read(buffer)) > 0) {
				f.write(buffer, 0, bytesRead);
			}// while
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				ins.close();
				f.close();
			} catch (Exception ex) {
			}
		}
	}// fn

	public static void writeUTF8ToFile(String filepath, String s) {
		if (null == filepath || null == s || s.length() < 1) {
			return;
		}
		try {
			byte[] bts = s.getBytes("UTF-8");
			writeToFile(filepath, bts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(String filepath, byte[] bts) {
		if (null == filepath || null == bts) {
			return;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bts);
		writeToFile(filepath, bis);
	}

	public static void writeToFile(String filepath, InputStream ins) {
		if (null == filepath || filepath.length() < 1) {
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(filepath);
			BufferedInputStream bis = null;
			if (ins instanceof BufferedInputStream) {
				bis = (BufferedInputStream) ins;
			} else {
				bis = new BufferedInputStream(ins);
			}
			byte[] buffer = new byte[512];
			try {
				int bytesRead = 0;
				while ((bytesRead = bis.read(buffer)) > 0) {
					fos.write(buffer, 0, bytesRead);
				}// while
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bis.close();
					ins.close();
					fos.close();
				} catch (Exception ex) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static long getFileSize(File f) {
		long ret = 0;
		if (f == null) {
			return ret;
		}
		if (!f.exists()) {
			return ret;
		}
		if (f.isFile()) {
			ret = f.length();
		} else if (f.isDirectory()) {
			for (File o : f.listFiles()) {
				ret += getFileSize(o);
			}
		}
		return ret;
	}

	public static long getFileSize(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return 0;
		}
		return getFileSize(new File(filepath));
	}

	public static void writeToRandomAccessFile(RandomAccessFile f, byte[] bts) {
		if (null == f || null == bts) {
			return;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bts);
		writeToRandomAccessFile(f, bis);
	}

	public static void writeToRandomAccessFile(String filepath, byte[] bts) {
		if (null == filepath || null == bts) {
			return;
		}
		try {
			writeToRandomAccessFile(new RandomAccessFile(filepath, "rw"), bts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeUTF8ToRandomAccessFile(RandomAccessFile f, String s) {
		if (null == f || null == s || s.length() < 1) {
			return;
		}
		try {
			byte[] bts = s.getBytes("UTF-8");
			writeToRandomAccessFile(f, bts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeUTF8ToRandomAccessFile(String filepath, String s) {
		if (null == filepath || null == s || s.length() < 1) {
			return;
		}
		try {
			writeUTF8ToRandomAccessFile(new RandomAccessFile(filepath, "rw"), s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void renameTo(File f1, File f2) {
		if (null == f1 || null == f2 || (!f1.exists())) {
			return;
		}
		f1.renameTo(f2);
	}

	public static void renameTo(String f1, String f2) {
		if (null == f1 || null == f2) {
			return;
		}
		renameTo(new File(f1), new File(f2));
	}

	public static byte[] readInputStream(InputStream ins) {
		if (ins == null) {
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		try {
			int bytesRead = 0;
			while ((bytesRead = ins.read(buffer)) > 0) {
				bos.write(buffer, 0, bytesRead);
			}// while
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ins.close();
			} catch (Exception ex) {
			}
		}
		return bos.toByteArray();
	}

	public static byte[] readFile(File f) {
		if (null == f) {
			return null;
		}
		return readInputStream(getFileInputStream(f));
	}

	public static byte[] readFile(String filepath) {
		if (null == filepath || filepath.length() < 1) {
			return null;
		}
		File f = new File(filepath);
		if (f.exists() && f.isFile()) {
			return readFile(f);
		}
		return null;
	}

	public static String readFileForString(File f) {
		byte[] bts = readFile(f);
		return new String(bts);
	}

	public static String readFileForString(File f, String encode) {
		if (null == f || null == encode) {
			return null;
		}
		byte[] bts = readFile(f);
		try {
			return new String(bts, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readFileForString(String filepath) {
		byte[] bts = readFile(filepath);
		if (null == bts) {
			return null;
		}
		return new String(bts);
	}

	public static String readFileForString(String filepath, String encode) {
		if (null == filepath || null == encode) {
			return null;
		}
		return readFileForString(new File(filepath), encode);
	}

	public static void saveObject(Object o, OutputStream fos) {
		if (null == o || null == fos) {
			return;
		}
		BufferedOutputStream bos = null;
		if (fos instanceof BufferedOutputStream) {
			bos = (BufferedOutputStream) fos;
		} else {
			bos = new BufferedOutputStream(fos);
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveObject(Object o, String filepath) {
		if (null == filepath || null == o) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		saveObject(o, fos);
	}

	public static String readFully(Reader reader) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			int count;
			while ((count = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, count);
			}
			return writer.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * Deletes the contents of {@code dir}. Throws an IOException if any file
	 * could not be deleted, or if {@code dir} is not a readable directory.
	 */
	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new IOException("not a readable directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (!file.delete()) {
				throw new IOException("failed to delete file: " + file);
			}
		}
	}

	public static void closeQuietly(/* Auto */Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (RuntimeException rethrown) {
				throw rethrown;
			} catch (Exception ignored) {
			}
		}
	}

	/* ---------------------- zip ------------------------------ */

	public static void unzipFile(InputStream stream, String rootDirectory) {
		FileOutputStream out;
		byte buf[] = new byte[16384];

		try {
			ZipInputStream zis = new ZipInputStream(stream);
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					File newDir = new File(rootDirectory + File.separator + entry.getName());
					newDir.mkdir();
				} else {
					String name = entry.getName();
					File outputFile = new File(rootDirectory + File.separator + name);
					String outputPath = outputFile.getCanonicalPath();
					name = outputPath.substring(outputPath.lastIndexOf("/") + 1);
					outputPath = outputPath.substring(0, outputPath.lastIndexOf("/"));
					File outputDir = new File(outputPath);
					outputDir.mkdirs();
					outputFile = new File(outputPath, name);
					outputFile.createNewFile();
					out = new FileOutputStream(outputFile);

					int numread = 0;
					do {
						numread = zis.read(buf);
						if (numread <= 0) {
							break;
						} else {
							out.write(buf, 0, numread);
						}
					} while (true);
					out.close();
				}
				entry = zis.getNextEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addFile2Zip(byte[] bytes, String entryName) {
		ZipOutputStream zos = null;
		ByteArrayInputStream bis = null;
		try {
			zos = new ZipOutputStream(new ByteArrayOutputStream());
			zos.putNextEntry(new ZipEntry(entryName));
			bis = new ByteArrayInputStream(bytes);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = bis.read(buf, 0, len)) != -1) {
				zos.write(buf, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.closeEntry();
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public byte[] getFileFromZip(String zipFilePath, String entryName) {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(new File(zipFilePath));
			ZipEntry ze = zipFile.getEntry(entryName);
			InputStream in = zipFile.getInputStream(ze);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = in.read(buf)) != -1) {
				bos.write(buf, 0, length);
			}
			byte[] bytes = bos.toByteArray();
			return bytes;
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

} // cls
