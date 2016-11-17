package com.example.test22.threadaction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 并发编程，FutureTask,Callable返回结果
 * 
 * @author zc
 * 
 */
public class Poster1 {

	public Poster1() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(2);
		MyTask task = new MyTask();
		Future<Integer> f = es.submit(task);
		String ss = "ss"+null;
		try {
			//这种方法获取结果，如果超时，则后面的代码（这里是System.out不执行）不执行，然后
			//抛出超时异常，而且并没有返回null,其实可以给变量初始化，如果没有初始化其实null的只是变量而已。
			ss = ss + f.get(1000l, TimeUnit.MILLISECONDS);
			System.out.println("线程的返回结果" + ss);
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
			System.out.println("超时了，线程的返回结果" + ss);
			e.printStackTrace();
		}
		MyTask task2 = new MyTask();
		FutureTask<Integer> ft = new FutureTask<Integer>(task2);
		es.submit(ft);
		es.shutdown();
		// new Thread(ft).start();
		try {
			System.out.println("ft.get()==" + ft.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("任务结束");

	}

	static class MyTask implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			System.out.println("线程 " + Thread.currentThread().getName()
					+ "准备返回结果");
			Thread.sleep(2000);
			int sum = 0;
			for (int i = 1; i <= 100; i++) {
				sum = sum + i;
			}
			return sum;
		}
	}

}
