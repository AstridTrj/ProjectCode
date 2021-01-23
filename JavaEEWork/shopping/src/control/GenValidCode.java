package control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;


public class GenValidCode {
	private int width = 100;
	private int height = 30;
	private Random random = new Random();
	private String[] fontName = {"����", "���Ŀ���", "����", "΢���ź�", "����_GB2312"};
	private String codes = "0123456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	private String validCode = "";
	
	//�����ɫ����
	private Color randomColor(){
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);
		
		return new Color(r, g, b);
	}
	//�����������
	private Font randomFont(){
		int index = random.nextInt(fontName.length);
		String font = fontName[index];
		int form = random.nextInt(4);
		int size = random.nextInt(7) + 25;
		return new Font(font, form, size);
	}
	//���Ƹ�����
	private void drawLine(BufferedImage image){
		int lineNum = 4;
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for(int i = 0; i < lineNum; i++){
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			graphics2d.setStroke(new BasicStroke(1F));
			graphics2d.setColor(this.randomColor());
			graphics2d.drawLine(x1, y1, x2, y2);
		}
	}
	//��������ַ�
	private char randomChar(){
		int index = random.nextInt(codes.length());
		return codes.charAt(index);
	}
	//����ͼ��
	public BufferedImage createImage(){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		graphics2d.setColor(Color.WHITE);
		graphics2d.fillRect(0, 0, width, height);
		return image;
	}
	//��֤��ͼƬ�Ļ�ȡ
	public BufferedImage getValidImage(){
		BufferedImage image = this.createImage();
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		//����5λ����֤��
		for(int i = 0; i < 5; i++){
			String ch = this.randomChar() + "";
			validCode += ch;
			float x = i * 1.0F * width / 5;
			graphics2d.setFont(this.randomFont());
			graphics2d.setColor(this.randomColor());
			graphics2d.drawString(ch, x, height-5);
		}
		this.drawLine(image);
		return image;
	}
	//��ȡ��֤��
	public String getValidCode() {
		return validCode;
	}
	// ͼƬת���������
    public void output (BufferedImage image, OutputStream out) 
                throws IOException {
        ImageIO.write(image, "JPEG", out);
    }
}





