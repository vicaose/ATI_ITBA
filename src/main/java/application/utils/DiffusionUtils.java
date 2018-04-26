package application.utils;

import domain.Image;
import domain.Image.ChannelType;

public class DiffusionUtils {
	
	public static Image isotropicDiffusion(Image original, int iterations) {
		return anisotropicDiffusion(original, iterations, new BorderDetectorFunction() {	
			@Override
			public double g(double pixel) {
				return 1;
			}
		});
	}
		
	public static Image anisotropicDiffusion(Image original, int iterations, BorderDetectorFunction pf) {
		Image diffused = original.shallowClone();
		for(int i = 0; i < iterations; i++) {
			for(int x = 0; x < diffused.getWidth(); x++) {
				for(int y = 0; y < diffused.getHeight(); y++) {
					diffusePixel(original, diffused, x, y, pf);
				}
			}
			original = (Image) diffused.clone();
		}
		return diffused;
	}
	
	private static void diffusePixel(Image original, Image diffused, int i, int j, BorderDetectorFunction pf) {
		for(ChannelType channel: Image.rgbValues()){
			double oldValueIJ = original.getPixel(i, j, channel);
	
			double DnIij = i > 0 ? original.getPixel(i - 1, j, channel)
					- oldValueIJ : 0;
			double DsIij = i < original.getWidth() - 1 ? original.getPixel(i + 1, j, channel)
					- oldValueIJ : 0;
			double DeIij = j < original.getHeight() - 1 ? original.getPixel(i, j + 1, channel)
					- oldValueIJ : 0;
			double DoIij = j > 0 ? original.getPixel(i, j - 1, channel)
					- oldValueIJ : 0;
	
			double Cnij = pf.g(DnIij);
			double Csij = pf.g(DsIij);
			double Ceij = pf.g(DeIij);
			double Coij = pf.g(DoIij);
	
			double DnIijCnij = DnIij * Cnij;
			double DsIijCsij = DsIij * Csij;
			double DeIijCeij = DeIij * Ceij;
			double DoIijCoij = DoIij * Coij;
	
			double lambda = 0.25;
			double newValueIJ = oldValueIJ + lambda
					* (DnIijCnij + DsIijCsij + DeIijCeij + DoIijCoij);
			diffused.setPixel(i, j, channel, newValueIJ);
		}
	}
	
	public static class LeclercDetector implements BorderDetectorFunction {
		private double sigma;
		public LeclercDetector(double sigma){
			this.sigma = sigma;
		}
		@Override
		public double g(double x) {
			return Math.exp(-Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2));
		}
	}
	
	public static class LorentzDetector implements BorderDetectorFunction {

		private double sigma;

		public LorentzDetector(double sigma){
			this.sigma = sigma;
		}

		@Override
		public double g(double x) {
			return 1/((Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2)) + 1);
		}

	}
}
