package net.pyraetos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CityGen extends JFrame{

	private BufferedImage img;
	private int pixels[];
	
	public static void main(String[] args){
		new CityGen();
		
	}
	
	public CityGen() {
		pixels = new int[1024 * 1024];
		City city = new City(City.LARGE);
		for (int i = 0; i < 1024; i++) {
			for (int j = 0; j < 1024; j++) {
				pixels[i * 1024 + j] = colorOf(city.valueAt(toCity(i), toCity(j)));
			}
		}
		img = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, 1024, 1024, pixels, 0, 1024);
		
		
		try {
			File file = new File("city.png");
			if(!file.exists())
				file.createNewFile();
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("CityGen");
		JLabel label = new JLabel(new ImageIcon("city.png"));
		add(label);
		pack();
		setVisible(true);
	}
	
	public static int toCity(int n) {
		return n/4;
	}
	
	public static int toImg(int n) {
		return n*4;
	}

	public static int colorOf(int cityValue) {
		switch(cityValue) {
		case(City.NOTHING):{
			return Color.WHITE.getRGB();
		}
		case(City.ROAD):{
			return Color.BLACK.getRGB();
		}
		case(City.HOUSE):{
			return Color.RED.getRGB();
		}
		case(City.SPECIAL):{
			return Color.MAGENTA.getRGB();
		}default:{
			return Color.CYAN.getRGB();
		}
		}
	}
	
}
