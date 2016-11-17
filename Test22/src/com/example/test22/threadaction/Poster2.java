package com.example.test22.threadaction;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 * 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
 * 另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
 * Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。
 * 
 * 
 * 参考http://www.cnblogs.com/dolphin0520/p/3920397.html
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
	 * 使用CountDownLatch来使到当前的线程，必须等待其它线程都执行完任务
	 * 后才执行后续的代码
	 */
	public static  void testCountDownLatch(){
		final CountDownLatch cdl = new CountDownLatch(2);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("线程a："+Thread.currentThread().getName()+"正在执行任务");
					Thread.sleep(2000);
				    System.out.println("线程a："+Thread.currentThread().getName()+"完成任务了");
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
					System.out.println("线程b："+Thread.currentThread().getName()+"正在执行任务");
					Thread.sleep(2000);
				    System.out.println("线程b："+Thread.currentThread().getName()+"完成任务了");
				    cdl.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		try {
			System.out.println("在等待a,b线程完成任务");
			cdl.await();
			System.out.println("a,b线程都完成任务了");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * CyclicBarrier字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后
	 * 再全部同时执行.　假若有若干个线程都要进行写数据操作，并且只有所有线程都完成写数据操作之后，
	 * 这些线程才能继续做后面的事情，此时就可以利用CyclicBarrier了
	 */
	public static void testCycliBarrier(){
		int parties = 3;
		CyclicBarrier cb = new CyclicBarrier(parties);
		for(int i=0;i<parties;i++){
			new WriteData(cb).start();
		}
		//当所有线程都到达barrier状态后，会从这些线程中选择一个线程去执行Runnable
		CyclicBarrier cb2 = new CyclicBarrier(parties, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("所有线程都完成任务之后，"+Thread.currentThread().getName()+"执行这里的工作");
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
				System.out.println("线程"+Thread.currentThread().getName()+"正在写数据");
				Thread.sleep(3000);
				System.out.println("线程"+Thread.currentThread().getName()+"写数据完成了，在等待其它线程");
				cb.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println("所有线程写数据都完成了，线程"+Thread.currentThread().getName()+"可以干自己的事了");
		}
	}
	/**
	 * Semaphore 信号量 Semaphore可以控制同时访问的线程个数，通过 acquire() 获取一个许可，
	 * 如果没有就等待，而 release() 释放一个许可
	 */
	public static void testSemaphore(){
		//假如只有3台电脑，但是有6个人想用电脑
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
				System.out.println("线程"+Thread.currentThread().getName()+"想使用电脑");
				sp.acquire();
				System.out.println("线程"+Thread.currentThread().getName()+"在使用电脑");
				Thread.sleep(3000);
				System.out.println("线程"+Thread.currentThread().getName()+"不用电脑了");
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
