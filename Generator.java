import java.util.Random;
import java.lang.Math;

public class Generator {
	public static void main(String[] args) {
		int ID=0;
		int AAT=0;
		double M1 = 20;
		double STD1 = 3;
		double L = 5;
		Random random = new Random(System.currentTimeMillis());
		// generate 1000 Gaussians and Poisson values
		double gaussianValue = random.nextGaussian() * STD1 + M1;
		int gV=(int) Math.ceil(gaussianValue);
		System.out.println(ID+ "," + gV + ","
				+ AAT);
		for (int i = 1; i < 1000; i++) {
			ID++;
			gaussianValue = random.nextGaussian() * STD1 + M1;
			gV=(int) Math.ceil(gaussianValue);
			int poissonValue = Generator.myPoisson(L, random);
			AAT+=poissonValue;
			System.out.println(ID+ "," + gV + ","
					+ AAT);
		}// loop
	}// main

	public static int myPoisson(double mean, Random random) {
		int k = 0;
		double p = 1.0;
		double expLambda = Math.exp(-mean);
		do {
			k++;
			p *= random.nextDouble();
		} while (p >= expLambda);
		return k - 1;
	}// myPoisson
}// Generator