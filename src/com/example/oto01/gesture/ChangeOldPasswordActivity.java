package com.example.oto01.gesture;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.BaseActivity;
import com.example.oto01.R;
import com.example.oto01.SysApplication;
import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.LockPatternView.Cell;
import com.example.oto01.gesture.LockPatternView.DisplayMode;
import com.example.oto01.services.LoginManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 创建手势密码第二步
 * 
 * @类描述:
 * @项目名称: 社区e商户
 * @类名称: ChangeOldPasswordActivity
 * @包名称: com.example.oto01.changeMessage
 * @author: cym
 * @date: 2016-11-16下午2:31:53
 */
@SuppressLint("ResourceAsColor")
public class ChangeOldPasswordActivity extends BaseActivity implements
		OnClickListener {
	private LockPatternView mLockPatternView;// 手势密码的九宫格
	// private int mFailedPatternAttemptsSinceLastTimeout = 0;
	// private CountDownTimer mCountdownTimer = null;
	private int usrNameGu;// 店铺id
	private String findMode;// 手势密码保存的String类型字符串
	private Stage mUiStage = Stage.Introduction;// 给枚举类型就行默认值的设定
	protected List<LockPatternView.Cell> mChosenPattern = null;// 定义手势密码存储的list
	private Animation mShakeAnim;// 与上一次保存的手势密码显示不一致的时候，进行显示的动画
	@ViewInject(R.id.gesturepwd_create_text)
	private TextView mHeaderText;// 手势密码上面的文字
	private static final int ID_EMPTY_MESSAGE = -1;// 控件未存在， 或者出现问题的时候
	private static final String KEY_UI_STAGE = "uiStage";// 保存手势的一种标识
	private static final String KEY_PATTERN_CHOICE = "chosenPattern";// 在退出这个页面或者退到桌面需要保存的手势密码
	private SysApplication app;// application类的定义
	private GesturePasswordDBService dao;// 进行保存的手势密码的数据库
	private RelativeLayout rl_success;// 成功的布局
	private List<Cell> cells = new ArrayList<LockPatternView.Cell>();
	// 标题
	@ViewInject(R.id.title_font)
	private TextView title_font;
	// 重设
	@ViewInject(R.id.tv_cancle)
	private TextView tv_cancle;
	/**
	 * 在帮助屏幕中使用的patten来显示如何绘制图案。
	 */
	private final List<LockPatternView.Cell> mAnimatePattern = new ArrayList<LockPatternView.Cell>();

	/**
	 * 左下角按钮的状态
	 */
	enum LeftButtonMode {// 取消|重绘
		Cancel(R.string.cancel, true), CancelDisabled(R.string.cancel, false), Retry(
				R.string.lockpattern_retry_button_text, true), RetryDisabled(
				R.string.lockpattern_retry_button_text, false), Gone(
				ID_EMPTY_MESSAGE, false);

		/**
		 * @param text
		 *            此模式的显示文本
		 * @param enabled
		 *            按钮的状态
		 */
		LeftButtonMode(int text, boolean enabled) {
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * 右下角按钮的状态
	 */
	enum RightButtonMode {// 继续|确定
		Continue(R.string.lockpattern_continue_button_text, true), ContinueDisabled(
				R.string.lockpattern_continue_button_text, false), Confirm(
				R.string.lockpattern_confirm_button_text, true), ConfirmDisabled(
				R.string.lockpattern_confirm_button_text, false);
		/**
		 * @param text
		 *            此模式的显示文本
		 * @param enabled
		 *            按钮的状态
		 */
		RightButtonMode(int text, boolean enabled) {
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * 跟踪用户在选择模式时的内部逻辑。
	 */
	protected enum Stage {

		Introduction(R.string.gesture_password_guide_text1,
				LeftButtonMode.Cancel, RightButtonMode.ContinueDisabled,
				ID_EMPTY_MESSAGE, true), ChoiceTooShort(
				R.string.lockpattern_recording_incorrect_too_short,
				LeftButtonMode.Retry, RightButtonMode.ContinueDisabled,
				ID_EMPTY_MESSAGE, true), FirstChoiceValid(
				R.string.lockpattern_pattern_entered_header,
				LeftButtonMode.Retry, RightButtonMode.Continue,
				ID_EMPTY_MESSAGE, false), ConfirmWrong(
				R.string.lockpattern_need_to_unlock_wrong,
				LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled,
				ID_EMPTY_MESSAGE, true), ChoiceConfirmed(
				R.string.lockpattern_pattern_confirmed_header,
				LeftButtonMode.Cancel, RightButtonMode.Confirm,
				ID_EMPTY_MESSAGE, false);

		/**
		 * @param headerMessage
		 *            最上面的文本
		 * @param leftMode
		 *            左下角的按钮
		 * @param rightMode
		 *            右下角的按钮
		 * @param footerMessage
		 *            页脚消息
		 * @param patternEnabled
		 *            是否启用小部件
		 */
		Stage(int headerMessage, LeftButtonMode leftMode,
				RightButtonMode rightMode, int footerMessage,
				boolean patternEnabled) {
			this.headerMessage = headerMessage;
			this.leftMode = leftMode;
			this.rightMode = rightMode;
			this.footerMessage = footerMessage;
			this.patternEnabled = patternEnabled;
		}

		final int headerMessage;// 最上面的文本
		final LeftButtonMode leftMode;// 左下角的按钮
		final RightButtonMode rightMode;// 右下角的按钮
		final int footerMessage;// 页脚消息
		final boolean patternEnabled;// 是否启用小部件
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oldgesturepassword_unlock);
		// 初始化IOC注解（完全注解）,不需要再写findViewById的代码，其中R.id.txt_test是控件ID
		ViewUtils.inject(this);
		title_font.setText("手势密码");
		tv_cancle.setOnClickListener(this);
		app = (SysApplication) getApplication();
		rl_success = (RelativeLayout) findViewById(R.id.rl_success);
		// 九宫格的布局
		mLockPatternView = (LockPatternView) findViewById(R.id.gesturepwd_create_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		// 去掉手势密码的震动效果
		mLockPatternView.setTactileFeedbackEnabled(false);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		dao = new GesturePasswordDBService(this);
		LoginManager lm = LoginManager
				.getInstance(ChangeOldPasswordActivity.this);
		usrNameGu = lm.getLoginId();
		findMode = getIntent().getStringExtra("patternToString");
		mHeaderText.setText("再次绘制解锁图案");
		mHeaderText.setTextColor(Color.parseColor("#333333"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// 存储mUiStage的值
		outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
		if (findMode != null) {
			// 如果九宫格不为空的时候 进行存储 相应的画的内容
			outState.putString(KEY_PATTERN_CHOICE, findMode);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 清除手势密码
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	// 判断两次是不是一致
	private boolean isPatten(List<Cell> pattern, List<Cell> cells) {

		if (pattern.size() != cells.size()) {
			return false;
		}
		for (int i = 0; i < pattern.size(); i++) {
			if (pattern.get(i).equals(cells.get(i))) {
				if (i == (pattern.size() - 1)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 九宫格的监听
	 */
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternDetected(List<Cell> pattern) {

			cells = LockPatternUtils.stringToPattern(findMode);
			if (pattern == null)
				return;
			if (isPatten(pattern, cells)) {
				// 两次输入一致的时候
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);
				// 解锁成功返回需要用户信息的页面----
				loginSuccessToMainAcrtivity();
			} else {

				// 两次输入不一致的时候
				// 不一致的时候，重设按钮进行显示出来
				tv_cancle.setVisibility(View.VISIBLE);
				tv_cancle.setText("重设");
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				mHeaderText.setText("与上次绘制不一致，请重新绘制");
				mHeaderText.setTextColor(Color.parseColor("#ff0000"));
				mHeaderText.startAnimation(mShakeAnim);

				mLockPatternView.clearPattern();

			}

		}

		@Override
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		private void patternInProgress() {
			mHeaderText.setText(R.string.lockpattern_recording_inprogress);
			mHeaderText.setTextColor(Color.parseColor("#333333"));
		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {

		}

	};

	private void updateStage(Stage stage) {
		mUiStage = stage;

		// 相同的时候，九宫格是否可以点击
		if (stage.patternEnabled) {
			mLockPatternView.enableInput();
		} else {
			mLockPatternView.disableInput();
		}
		// 设置九宫格的模式，是正确
		mLockPatternView.setDisplayMode(DisplayMode.Correct);

		switch (mUiStage) {
		case Introduction:
			// 初始状态的时候
			mLockPatternView.clearPattern();
			mHeaderText.setText("再次绘制解锁图案");
			mHeaderText.setTextColor(Color.parseColor("#333333"));
			break;
		case ChoiceTooShort:
			// 少于两个点的时候
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case FirstChoiceValid:
			mHeaderText.setText("再次绘制解锁图案");
			mHeaderText.setTextColor(Color.parseColor("#333333"));
			break;
		case ConfirmWrong:
			// 与上一次不一致的时候
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case ChoiceConfirmed:
			break;
		}

	}

	// clear the wrong pattern unless they have started a new one
	// already
	private void postClearPatternRunnable() {
		// 清空九宫格中画的线
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		// 间隔两秒
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	// 确认密码设置成功的跳转
	private void loginSuccessToMainAcrtivity() {
		rl_success.setVisibility(View.VISIBLE);
		app.getLockPatternUtils().saveLockPattern(mChosenPattern);
		LoginManager lm = LoginManager
				.getInstance(ChangeOldPasswordActivity.this);
		shopsid = lm.getLoginId();
		// 得到用户名和将手势密码转换成string类型,进行保存的就是第一次进行设置的手势密码
		String patternToString = findMode;
		// 把手势密码 存储到数据库中
		dao.saveSearchTrace(getApplicationContext(), shopsid + "",
				patternToString);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				rl_success.setVisibility(View.GONE); // view是要隐藏的控件
				finish();
			}
		}, 1000); // 3000毫秒后执行

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_cancle:
			// 重设
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					CreateGesturePasswordActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	// private void toLoginActivity() {
	// // IsOut.setIsOut(this, true);
	// Intent intent = new Intent(ChangeOldPasswordActivity.this,
	// LoginActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
	// startActivity(intent);
	// ToastUtil.show(this, "旧手势密码输入错误");
	// // AnimationUtil.finishActivityAnimation(ChangeOldPasswordActivity.this);
	// }

	// Runnable attemptLockout = new Runnable() {
	//
	// @Override
	// public void run() {
	// mLockPatternView.clearPattern();
	// mLockPatternView.setEnabled(false);
	// mCountdownTimer = new CountDownTimer(
	// LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {
	//
	// @Override
	// public void onTick(long millisUntilFinished) {
	// int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
	// if (secondsRemaining > 0) {
	// mHeadTextView.setText(secondsRemaining + " 秒后重试");
	// } else {
	// mHeadTextView.setText(ChangeOldPasswordActivity.this
	// .getString(R.string.gesture_drawPwd));
	// mHeadTextView.setTextColor(Color.WHITE);
	// }
	//
	// }
	//
	// @Override
	// public void onFinish() {
	// mLockPatternView.setEnabled(true);
	// mFailedPatternAttemptsSinceLastTimeout = 0;
	// }
	// }.start();
	// }
	// };
}
