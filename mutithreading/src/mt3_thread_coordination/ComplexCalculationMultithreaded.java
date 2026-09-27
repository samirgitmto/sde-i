package mt3_thread_coordination;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ComplexCalculationMultithreaded {
	
    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
        	// BigInteger bi = BigDecimal.valueOf(d).toBigInteger();
        	 Double res = Math.pow(base.doubleValue(), power.doubleValue());
        	 this.result = BigDecimal.valueOf(res).toBigInteger();
        }
        
        /*
         * recommended
        @Override
        public void run() {
            for(BigInteger i = BigInteger.ZERO;
                i.compareTo(power) !=0;
                i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
        }
        */
        
        public BigInteger getResult() { return result; }
    }	
	public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
		
		PowerCalculatingThread powerCalculatingThread1 = new PowerCalculatingThread(base1, power1);
		PowerCalculatingThread powerCalculatingThread2 = new PowerCalculatingThread(base2, power2);
        powerCalculatingThread1.start();
        powerCalculatingThread2.start();
		
        powerCalculatingThread1.join();
        powerCalculatingThread2.join();
		
		result = powerCalculatingThread1.getResult().add(powerCalculatingThread2.getResult());
        
        return result;
    }

	public static void main(String[] args) {
		BigInteger a = new BigInteger("12345678901234567890");
        BigInteger b = BigInteger.valueOf(987654321L);
        BigInteger c = new BigInteger("FF", 16); // 255 in decimal
        BigInteger base1 = new BigInteger("10");
		BigInteger power1 = new BigInteger("2");
		BigInteger base2 = new BigInteger("10");
		BigInteger power2 = new BigInteger("2");
		
		ComplexCalculationMultithreaded obj = new ComplexCalculationMultithreaded();
		BigInteger result = null;
		try {
			result = obj.calculateResult(base1, power1, base2, power2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
	}

	
}