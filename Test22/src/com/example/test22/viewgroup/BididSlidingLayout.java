package com.example.test22.viewgroup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class BididSlidingLayout extends RelativeLayout implements OnTouchListener {
	/**
	 * ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶ�
	 */
	public static final int SNAP_VELOCITY=200;
	/**
	 * ����״̬��һ�֣���ʾδ�����κλ���
	 */
	public static final int DO_NOTHING=0;
	/**
	 * ����״̬��һ�֣���ʾ���ڻ�����˵�
	 */
	public static final int SHOW_LEFT_MENU=1;
	/**
	 * ����״̬��һ�֣���ʾ���ڻ����Ҳ˵�
	 */
	public static final int SHOW_RIGHT_MENU=2;
	/**
	 * �����������˵�
	 */
	public static final int HIDE_LEFT_MENU=3;
	/**
	 * ���������Ҳ�˵�
	 */
	public static final int HIDE_RIGHT_MENU=4;
	/**
	 * ��¼��ǰ�Ļ���״̬
	 */
	private int slideState;
	/**
	 * ��Ļ���
	 */
	private int screenWidth;
	/**
	 * �ڱ��ж�����֮ǰ�û���ָ�����ƶ������ֵ
	 */
	private int touchSlop;
	/**
	 * ��¼��ָ����ʱ�ĺ�����
	 */
	private float xDown;
	/**
	 * ��¼��ָ����ʱ�ĺ�����
	 */
	private float yDown;
	/**
	 *��¼ ��ָ�ƶ�ʱ��x����
	 */
	private float xMove;
	/**
	 * ��¼��ָ�ƶ�ʱ������
	 */
	private float yMove;
	/**
	 * ��¼��ָ̧��ʱ�ĺ�����
	 */
	private float xUp;
	/**
	 * ���˵���ǰ����ʾ�������أ�ֻ����ȫ��ʾ�����زŻ���Ĵ�ֵ
	 */
	private boolean isLeftMenuVisible;
	/**
	 * �Ҳ�˵���ǰ����ʾ�������أ�ֻ����ȫ��ʾ�����زŻ���Ĵ�ֵ
	 */
	private boolean isRightMenuVisible;
	/**
	 * �Ƿ����ڻ���
	 */
	private boolean isSliding;
	/**
	 * ���˵����ֶ���
	 */
	private View leftMenuLayout;
	/**
	 * �Ҳ�˵����ֶ���
	 */
	private View rightMenuLayout;
	/**
	 * ���ݲ��ֶ���
	 */
	private View contentLayout;
	/**
	 * ���ڼ��������¼���View
	 */
	private View mBindView;
	/**
	 * ���˵��Ĳ��ֲ���
	 */
	private MarginLayoutParams leftMenuLayoutParams;
	/**
	 * �Ҳ�˵��Ĳ��ֲ���
	 */
	private MarginLayoutParams rightMenuLayoutParams;
	/**
	 * ���ݲ��ֵĲ���
	 */
	private RelativeLayout.LayoutParams contentLayoutParams;
	/**
	 * ���ڼ�����ָ�������ٶ�
	 */
	private VelocityTracker mVelocityTracker;
	public BididSlidingLayout(Context context) {
		this(context,null);
	}

	public BididSlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	public BididSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	/**
	 * @param bindView ��Ҫ�󶨵�View����
	 */
	public void setScrollEvent(View bindView){
		mBindView = bindView;
		mBindView.setOnTouchListener(this);
	}
	/**
	 * �������������࣬�����ٶ��趨Ϊ-30
	 */
	public void scrollToLeftMenu(){
		new LeftMenuScrollTask().execute(-30);
	}
	/**
	 * ������������Ҳ�˵����棬�����ٶ��趨Ϊ-30
	 */
	public void scrollToRightMenu(){
		new RightMenuScollTask().execute(-30);
	}
	/**
	 * ����������˵����������ݽ��棬�����ٶ��趨Ϊ30
	 */
	public void scrollToContentFromLeftMenu(){
		new LeftMenuScrollTask().execute(30);
	}
	/**
	 * ��������Ҳ�˵����������ݽ��棬�����ٶ��趨Ϊ30
	 */
	public void scrollToContentFromRightMenu(){
		new RightMenuScollTask().execute(30);
	}
	/**���˵��Ƿ���ȫ��ʾ���������������д�ֵ��Ч
	 * @return ���˵���ȫ��ʾ�ͷ���true������false
	 */
	public boolean isLeftLayoutVisible(){
		return isLeftMenuVisible;
	}
	/**�Ҳ�˵��Ƿ���ȫ��ʾ���������������д�ֵ��Ч
	 * @return �Ҳ�˵���ȫ��ʾ����true�����򷵻�false
	 */
	public boolean isRightLayoutVisible(){
		return isRightMenuVisible;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			//��ȡ���˵��Ĳ��ֶ���
			leftMenuLayout = getChildAt(0);
			leftMenuLayoutParams = (MarginLayoutParams) leftMenuLayout.getLayoutParams();
			//�����˵���������λ��
			leftMenuLayoutParams.leftMargin = -leftMenuLayoutParams.width/3;
			leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
			
			rightMenuLayout = getChildAt(1);
			rightMenuLayoutParams = (MarginLayoutParams) rightMenuLayout.getLayoutParams();
			//��ȡ���ݲ��ֶ���
			contentLayout = getChildAt(2);
			contentLayoutParams = (LayoutParams) contentLayout.getLayoutParams();
			contentLayoutParams.width = screenWidth;
			contentLayout.setLayoutParams(contentLayoutParams);
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			//��ָ����ʱ����¼����ʱ������
			xDown = event.getRawX();
			yDown = event.getRawY();
			//������״̬��ʼ��ΪDO_NOTHING
			slideState = DO_NOTHING;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			int moveDistanceX = (int)(xMove-xDown);
			int moveDistanceY = (int)(yMove-yDown);
			//��鵱ǰ�Ļ���״̬
			checkSlideState(moveDistanceX,moveDistanceY);
			//���ݵ�ǰ�Ļ���״̬�������ƫ�����ݲ���
			switch(slideState){
			case SHOW_LEFT_MENU:
				contentLayoutParams.rightMargin = -moveDistanceX;
				checkLeftMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				
				leftMenuLayoutParams.leftMargin=-leftMenuLayoutParams.width/3+moveDistanceX/3;
				moveLeftMenu();
				leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
				break;
			case HIDE_LEFT_MENU:
				contentLayoutParams.rightMargin = -leftMenuLayoutParams.width-moveDistanceX;
				checkLeftMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				
				leftMenuLayoutParams.leftMargin=moveDistanceX/3;
				moveLeftMenu();
				leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
				break;
			case SHOW_RIGHT_MENU:
				contentLayoutParams.leftMargin = moveDistanceX;
				checkRightMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;
			case HIDE_RIGHT_MENU:
				contentLayoutParams.leftMargin = -rightMenuLayoutParams.width+moveDistanceX;
				checkRightMenuBorder();
				contentLayout.setLayoutParams(contentLayoutParams);
				break;
				default:break;
			}
			break;
		case MotionEvent.ACTION_UP:
			xUp = event.getRawX();
			int upDistanceX = (int)(xUp-xDown);
			if(isSliding){
				//��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ
				switch(slideState){
				case SHOW_LEFT_MENU:
					if(shouldScrollToLeftMenu()){
						scrollToLeftMenu();
					}else{
						scrollToContentFromLeftMenu();
					}
					break;
				case HIDE_LEFT_MENU:
					if(shouldScrollToContentFromLeftMenu()){
						scrollToContentFromLeftMenu();
					}else{
						scrollToLeftMenu();
					}
					break;
				case SHOW_RIGHT_MENU:
					if(shouldScrollToRightMenu()){
						scrollToRightMenu();
					}else{
						scrollToContentFromRightMenu();
					}
					break;
				case HIDE_RIGHT_MENU:
					if(shouldScrollToContentFromRightMenu()){
						scrollToContentFromRightMenu();
					}else{
						scrollToRightMenu();
					}
					break;
					default:break;
				}
			}else if(upDistanceX<touchSlop&&isLeftMenuVisible){
				//�����˵���ʾʱ������û����һ�����ݲ��֣���ֱ�ӹ��������ݽ���
				scrollToContentFromLeftMenu();
			}else if(upDistanceX<touchSlop&&isRightMenuVisible){
				//���Ҳ�˵���ʾʱ������û����һ�����ݲ��֣���ֱ�ӹ��������ݲ���
				scrollToContentFromRightMenu();
			}
			recycleVelocityTracker();
			break;
		}
		if(v.isEnabled()){
			if(isSliding){
				//���ڻ���ʱ�ÿؼ��ò�������
				unFocusBindView();
				return true;
			}
			if(isLeftMenuVisible||isRightMenuVisible){
				//�������Ҳ಼����ʾʱ�����󶨿ؼ����¼����ε�
				return true;
			}
			return false;
		}
		return true;
	}

	/** ��鵱ǰ�Ļ���״̬
	 * @param moveDistanceX
	 * @param moveDistanceY
	 */
	private void checkSlideState(int moveDistanceX, int moveDistanceY) {
		if(isLeftMenuVisible){
			if(!isSliding&&Math.abs(moveDistanceX)>=touchSlop&&moveDistanceX<0){
				isSliding = true;
				slideState = HIDE_LEFT_MENU;
			}
		}else if(isRightMenuVisible){
			if(!isSliding&&Math.abs(moveDistanceX)>=touchSlop&&moveDistanceX>0){
				isSliding = true;
				slideState = HIDE_RIGHT_MENU;
			}
		}else{
			if(!isSliding&&Math.abs(moveDistanceX)>=touchSlop&&moveDistanceX>0
					&&Math.abs(moveDistanceY)<touchSlop){
				isSliding = true;
				slideState =  SHOW_LEFT_MENU;
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				contentLayout.setLayoutParams(contentLayoutParams);
				
				//����û���Ӧ�������˵��������˵���ʾ���Ҳ�����
				leftMenuLayout.setVisibility(View.VISIBLE);
				rightMenuLayout.setVisibility(View.GONE);
			}else if(!isSliding&&Math.abs(moveDistanceX)>=touchSlop
					&&moveDistanceX<0&&Math.abs(moveDistanceY)<touchSlop){
				isSliding = true;
				slideState = SHOW_RIGHT_MENU;
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
				contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				contentLayout.setLayoutParams(contentLayoutParams);
				//����û���Ҫ��Ҳ�˵������Ҳ�˵���ʾ
				leftMenuLayout.setVisibility(View.GONE);
				rightMenuLayout.setVisibility(View.VISIBLE);
			}
		}
	}
	/**
	 * ���������м�����˵��ı߽�ֵ����ֹ�󶨲��ֻ�����Ļ
	 */
	private void checkLeftMenuBorder(){
		if(contentLayoutParams.rightMargin>0){
			contentLayoutParams.rightMargin=0;
		}else if(contentLayoutParams.rightMargin<-leftMenuLayoutParams.width){
			contentLayoutParams.rightMargin=-leftMenuLayoutParams.width;
		}
	}
	/**
	 * ���������м���Ҳ�˵��ı߽�ֵ����ֹ�󶨲��ֻ�����Ļ
	 */
	private void checkRightMenuBorder(){
		if(contentLayoutParams.leftMargin>0){
			contentLayoutParams.leftMargin=0;
		}else if(contentLayoutParams.leftMargin<-rightMenuLayoutParams.width){
			contentLayoutParams.leftMargin = -rightMenuLayoutParams.width;
		}
	}
	/**�ж��Ƿ�Ӧ�ý����˵�չʾ�����������ָ�ƶ�����������˵���ȵ�һ�룬��
	 * �����ٶȴ���snapVelocity,��Ѳ˵���ʾ����
	 * @return
	 */
	private boolean shouldScrollToLeftMenu(){
		return xUp-xDown>leftMenuLayoutParams.width/2||getScrollVelocity()>SNAP_VELOCITY;
	}
	/**�ж��Ƿ��Ҳ�˵�չʾ����
	 * @return
	 */
	private boolean shouldScrollToRightMenu(){
		return xDown-xUp>rightMenuLayoutParams.width/2||getScrollVelocity()>SNAP_VELOCITY;
	}
	/**�Ƿ��������˵�
	 * @return
	 */
	private boolean shouldScrollToContentFromLeftMenu(){
		return xDown-xUp>leftMenuLayoutParams.width/2||getScrollVelocity()>SNAP_VELOCITY;
	}
	/**�Ƿ������Ҳ�˵�
	 * @return
	 */
	private boolean shouldScrollToContentFromRightMenu(){
		return xUp-xDown>rightMenuLayoutParams.width/2||getScrollVelocity()>SNAP_VELOCITY;
	}

	/**����VelocityTracker���󣬲��������¼�����
	 * @param event
	 */
	private void createVelocityTracker(MotionEvent event) {
		if(mVelocityTracker==null){
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	/**��ȡ��ָ�ڰ󶨲����ϵĻ����ٶ�
	 * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ
	 */
	private int getScrollVelocity(){
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity =(int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}
	/**
	 * ����VelocityTracker����
	 */
	private void recycleVelocityTracker(){
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}
	/**
	 * ʹ�ÿ��Ի�ý���Ŀؼ��ڻ�����ʱ��ʧȥ����
	 */
	private void unFocusBindView(){
		if(mBindView!=null){
			mBindView.setPressed(false);
			mBindView.setFocusable(false);
			mBindView.setFocusableInTouchMode(false);
		}
	}
	class LeftMenuScrollTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... speed) {
			int rightMargin = contentLayoutParams.rightMargin;
			while(true){
				rightMargin = rightMargin+speed[0];
				if(rightMargin<-leftMenuLayoutParams.width){
					rightMargin = -leftMenuLayoutParams.width;
					break;
				}
				if(rightMargin>0){
					rightMargin=0;
					break;
				}
				publishProgress(rightMargin);
				//Ϊ��Ҫ�й���Ч��������˯��һ��ʱ��
				sleep(15);
			}
			if(speed[0]>0){
				isLeftMenuVisible = false;
			}else{
				isLeftMenuVisible = true;
			}
			isSliding = false;
			return rightMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... rightMargins) {
			contentLayoutParams.rightMargin = rightMargins[0];
			contentLayout.setLayoutParams(contentLayoutParams);
			unFocusBindView();
			//�ƶ����˵�
			if(slideState==SHOW_LEFT_MENU){
				leftMenuLayoutParams.leftMargin = (leftMenuLayoutParams.width+contentLayoutParams.rightMargin)/3;
			}else{
				leftMenuLayoutParams.leftMargin =- (leftMenuLayoutParams.width+contentLayoutParams.rightMargin)/3;
			}
			moveLeftMenu();
			leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
			
		}
		@Override
		protected void onPostExecute(Integer rightMargin) {
			contentLayoutParams.rightMargin = rightMargin;
			contentLayout.setLayoutParams(contentLayoutParams);
			
			if(isLeftMenuVisible){
				leftMenuLayoutParams.leftMargin = 0;
				leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
			}else{
				leftMenuLayoutParams.leftMargin = -leftMenuLayoutParams.width/3;
				leftMenuLayout.setLayoutParams(leftMenuLayoutParams);
			}
			
		}
		
	}
	class RightMenuScollTask extends AsyncTask<Integer,Integer,Integer>{

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = contentLayoutParams.leftMargin;
			//���ݴ�����ٶ����������棬����������߽�ֵʱ������ѭ��
			while(true){
				leftMargin = leftMargin+speed[0];
				if(leftMargin<-rightMenuLayoutParams.width){
					leftMargin = -rightMenuLayoutParams.width;
					break;
				}
				if(leftMargin>0){
					leftMargin=0;
					break;
				}
				publishProgress(leftMargin);
				sleep(15);
			}
			if(speed[0]>0){
				isRightMenuVisible = false;
			}else{
				isRightMenuVisible = true;
			}
			isSliding = false;
			return leftMargin;
		}
		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			contentLayoutParams.leftMargin = leftMargin[0];
			contentLayout.setLayoutParams(contentLayoutParams);
			unFocusBindView();
		}
		@Override
		protected void onPostExecute(Integer leftMargin) {
			contentLayoutParams.leftMargin = leftMargin;
			contentLayout.setLayoutParams(contentLayoutParams);
		}
	}
	/**ʹ��ǰ�߳�˯��ָ��ʱ��
	 * @param millis
	 */
	private void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void moveLeftMenu(){
		if(leftMenuLayoutParams.leftMargin>0){
			leftMenuLayoutParams.leftMargin = 0;
		}else if(leftMenuLayoutParams.leftMargin<-leftMenuLayoutParams.width/3){
			leftMenuLayoutParams.leftMargin=-leftMenuLayoutParams.width/3;
		}
	}
}
