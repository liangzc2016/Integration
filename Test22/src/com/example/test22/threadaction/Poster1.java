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
 * ������̣�FutureTask,Callable���ؽ��
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
			//���ַ�����ȡ����������ʱ�������Ĵ��루������System.out��ִ�У���ִ�У�Ȼ��
			//�׳���ʱ�쳣�����Ҳ�û�з���null,��ʵ���Ը�������ʼ�������û�г�ʼ����ʵnull��ֻ�Ǳ������ѡ�
			ss = ss + f.get(1000l, TimeUnit.MILLISECONDS);
			System.out.println("�̵߳ķ��ؽ��" + ss);
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
			System.out.println("��ʱ�ˣ��̵߳ķ��ؽ��" + ss);
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
		System.out.println("�������");

	}

	static class MyTask implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			System.out.println("�߳� " + Thread.currentThread().getName()
					+ "׼�����ؽ��");
			Thread.sleep(2000);
			int sum = 0;
			for (int i = 1; i <= 100; i++) {
				sum = sum + i;
			}
			return sum;
		}
	}

}
