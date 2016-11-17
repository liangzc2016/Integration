package com.example.test22.threadaction;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**CountDownLatch��CyclicBarrier���ܹ�ʵ���߳�֮��ĵȴ���ֻ�������ǲ��ص㲻ͬ��
 * CountDownLatchһ������ĳ���߳�A�ȴ����ɸ������߳�ִ��������֮������ִ�У�
 * ��CyclicBarrierһ������һ���̻߳���ȴ���ĳ��״̬��Ȼ����һ���߳���ͬʱִ�У�
 * ���⣬CountDownLatch�ǲ��ܹ����õģ���CyclicBarrier�ǿ������õġ�
 * Semaphore��ʵ�����е����ƣ���һ�����ڿ��ƶ�ĳ����Դ�ķ���Ȩ�ޡ�
 * 
 * 
 * �ο�http://www.cnblogs.com/dolphin0520/p/3920397.html
 * @author zc
 */
public class Poster2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testCountDownLatch();
		testCycliBarrier();
//		testSemaphore();
	}
	/**
	 * ʹ��CountDownLatch��ʹ����ǰ���̣߳�����ȴ������̶߳�ִ��������
	 * ���ִ�к����Ĵ���
	 */
	public static  void testCountDownLatch(){
		final CountDownLatch cdl = new CountDownLatch(2);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("�߳�a��"+Thread.currentThread().getName()+"����ִ������");
					Thread.sleep(2000);
				    System.out.println("�߳�a��"+Thread.currentThread().getName()+"���������");
				    cdl.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("�߳�b��"+Thread.currentThread().getName()+"����ִ������");
					Thread.sleep(2000);
				    System.out.println("�߳�b��"+Thread.currentThread().getName()+"���������");
				    cdl.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		try {
			System.out.println("�ڵȴ�a,b�߳��������");
			cdl.await();
			System.out.println("a,b�̶߳����������");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * CyclicBarrier������˼�ػ�դ����ͨ��������ʵ����һ���̵߳ȴ���ĳ��״̬֮��
	 * ��ȫ��ͬʱִ��.�����������ɸ��̶߳�Ҫ����д���ݲ���������ֻ�������̶߳����д���ݲ���֮��
	 * ��Щ�̲߳��ܼ�������������飬��ʱ�Ϳ�������CyclicBarrier��
	 */
	public static void testCycliBarrier(){
		int parties = 3;
		CyclicBarrier cb = new CyclicBarrier(parties);
		for(int i=0;i<parties;i++){
			new WriteData(cb).start();
		}
		//�������̶߳�����barrier״̬�󣬻����Щ�߳���ѡ��һ���߳�ȥִ��Runnable
		CyclicBarrier cb2 = new CyclicBarrier(parties, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("�����̶߳��������֮��"+Thread.currentThread().getName()+"ִ������Ĺ���");
			}
		});
		for(int i=0;i<parties;i++){
			new WriteData(cb2).start();
		}
	}
	static class WriteData extends Thread{
		CyclicBarrier cb ;
		public WriteData(CyclicBarrier cb){
			this.cb = cb;
		}
		@Override
		public void run() {
			try {
				System.out.println("�߳�"+Thread.currentThread().getName()+"����д����");
				Thread.sleep(3000);
				System.out.println("�߳�"+Thread.currentThread().getName()+"д��������ˣ��ڵȴ������߳�");
				cb.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println("�����߳�д���ݶ�����ˣ��߳�"+Thread.currentThread().getName()+"���Ը��Լ�������");
		}
	}
	/**
	 * Semaphore �ź��� Semaphore���Կ���ͬʱ���ʵ��̸߳�����ͨ�� acquire() ��ȡһ����ɣ�
	 * ���û�о͵ȴ����� release() �ͷ�һ�����
	 */
	public static void testSemaphore(){
		//����ֻ��3̨���ԣ�������6�������õ���
		int num = 3;
		Semaphore sp = new Semaphore(num);
		for(int i=0;i<num+3;i++){
			new Programer(sp).start();
		}
	}
	static class Programer extends Thread{
		Semaphore sp;
		public Programer(Semaphore sp){
			this.sp = sp;
		}
		@Override
		public void run() {
			try {
				System.out.println("�߳�"+Thread.currentThread().getName()+"��ʹ�õ���");
				sp.acquire();
				System.out.println("�߳�"+Thread.currentThread().getName()+"��ʹ�õ���");
				Thread.sleep(3000);
				System.out.println("�߳�"+Thread.currentThread().getName()+"���õ�����");
				sp.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public int add(int ...array){
		int sum=0;
		for(int i:array){
			sum+=i%100;
		}
		return sum%100;
	}

}
