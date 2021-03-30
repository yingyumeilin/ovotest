package com.example.oto01.gesture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.oto01.R;
import com.example.oto01.SysApplication;
import com.example.oto01.db.GesturePasswordDBService;
import com.example.oto01.gesture.LockPatternView.Cell;
import com.example.oto01.gesture.LockPatternView.DisplayMode;
import com.example.oto01.services.LoginManager;

/**
 * 
 * 
 * @类描述:创建手势密码第一步
 * @项目名称: 社区e商户
 * @类名称: CreateGesturePasswordActivity
 * @包名称: com.example.oto01.gesture
 * @author: cym
 * @date: 2016-12-1上午11:16:09
 */
public class CreateGesturePasswordActivity extends Activity {
	private static final int ID_EMPTY_MESSAGE = -1;// 控件未存在， 或者出现问题的时候
	private static final String KEY_UI_STAGE = "uiStage";// 保存手势的一种标识
	private static final String KEY_PATTERN_CHOICE = "chosenPattern";// 在退出这个页面或者退到桌面需要保存的手势密码
	private LockPatternView mLockPatternView;// 手势密码的九宫格
	// private Button mFooterRightButton;
	// private Button mFooterLeftButton;
	protected TextView mHeaderText;// 手势密码上面的文字
	private TextView title_font;// 大标题
	protected List<LockPatternView.Cell> mChosenPattern = null;// 定义手势密码存储的list
	private Stage mUiStage = Stage.Introduction;// 给枚举类型就行默认值的设定
	// private View mPreviewViews[][] = new View[3][3];
	private SysApplication app;// application类的定义
	private GesturePasswordDBService dao;// 数据库的定义
	private int shopsid;// 商铺号
	private Animation mShakeAnim;// 手势密码上面的文字，在不符合要求的时候，进行显示这个动画
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
		setContentView(R.layout.gesturepassword_create);
		// 设置标题文字
		title_font = (TextView) findViewById(R.id.title_font);
		title_font.setText("手势密码");
		// 初始化application
		app = (SysApplication) getApplication();
		// 初始化数据库
		dao = new GesturePasswordDBService(this);
		// 初始化演示动
		mAnimatePattern.add(LockPatternView.Cell.of(0, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(1, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 2));
		// 手势密码的九宫格
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_create_lockview);
		// 手势密码上面的文字
		mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);
		mHeaderText.setText("绘制解锁图案");
		// 手势密码上面的文字的动画设置
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		// 手势密码注册监听器
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		// 去掉手势密码的震动效果
		mLockPatternView.setTactileFeedbackEnabled(false);
		//
		// mFooterRightButton = (Button) this.findViewById(R.id.right_btn);
		// mFooterLeftButton = (Button) this.findViewById(R.id.reset_btn);

		// mFooterRightButton.setOnClickListener(this);
		// mFooterLeftButton.setOnClickListener(this);
		if (savedInstanceState == null) {
			// 这个activity没有保存状态的时候
			updateStage(Stage.Introduction);
			// updateStage(Stage.HelpScreen);
		} else {
			// 从以前的状态恢复
			// 给 KEY_PATTERN_CHOICE 对应的 value 赋值
			final String patternString = savedInstanceState
					.getString(KEY_PATTERN_CHOICE);

			if (patternString != null) {
				// 如果value不为空的时候，把存储的手势密码String类型转换成 list
				mChosenPattern = LockPatternUtils
						.stringToPattern(patternString);
			}
			// 更新之前存储的KEY_UI_STAGE 对应的value
			updateStage(Stage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 存储mUiStage的值
		outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
		if (mChosenPattern != null) {
			// 如果九宫格不为空的时候 进行存储 相应的画的内容
			outState.putString(KEY_PATTERN_CHOICE,
					LockPatternUtils.patternToString(mChosenPattern));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 点击手机物理返回键的时候
			// if (mUiStage == Stage.HelpScreen) {
			// updateStage(Stage.Introduction);
			// return true;
			// }
			finish();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && mUiStage == Stage.Introduction) {
			// updateStage(Stage.HelpScreen);
			// 当点击手机home键的时候，并且 枚举的状态是默认值
			return true;

		}
		return false;
	}

	/**
	 * 清除九宫格的记录
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	/**
	 * 点按九宫格的时候
	 */
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			// 清空九宫格
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			// 按住九宫格的时候
			patternInProgress();
		}

		@Override
		public void onPatternDetected(List<Cell> pattern) {
			// 按完九宫格的时候
			if (pattern == null)
				// 九宫格没有操作的时候
				return;
			if (mUiStage == Stage.ConfirmWrong) {
				// 九宫格设置的与上次不一致的时候
				if (mChosenPattern == null)
					throw new IllegalStateException(
							"null chosen pattern in stage 'need to confirm");
				if (mChosenPattern.equals(pattern)) {
					// 保存新的解锁图案
					updateStage(Stage.ChoiceConfirmed);
				} else {
					// 与上次不一致
					updateStage(Stage.ConfirmWrong);
				}
			} else if (mUiStage == Stage.Introduction
					|| mUiStage == Stage.ChoiceTooShort) {

				if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
					// 选择两个点，不满足 最少3个点的时候
					// 更新九宫格上面的文字，变成Stage.ChoiceTooShort
					updateStage(Stage.ChoiceTooShort);
				} else {
					// 成功满足条件的点
					mChosenPattern = new ArrayList<LockPatternView.Cell>(
							pattern);
					// 手势密码第一次保存的值
					updateStage(Stage.FirstChoiceValid);
					// 进行数据库存储
					saveChosenPatternAndFinish();

				}
			} else {
				throw new IllegalStateException("Unexpected stage " + mUiStage
						+ " when " + "entering the pattern.");
			}
		}

		@Override
		public void onPatternCleared() {
			// TODO Auto-generated method stub
			// 清空九宫格中画的东西
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		/**
		 * 
		 * TODO<按住九宫格的时候>
		 * 
		 * @throw
		 * @return void
		 */
		private void patternInProgress() {
			mHeaderText.setText(R.string.lockpattern_recording_inprogress);
			mHeaderText.setTextColor(Color.parseColor("#333333"));
			// mFooterLeftButton.setEnabled(false);
			// mFooterRightButton.setEnabled(false);
		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 
	 * TODO<更新状态>
	 * 
	 * @throw
	 * @return void
	 */
	private void updateStage(Stage stage) {
		mUiStage = stage;
		if (stage == Stage.ChoiceTooShort) {
			// 点击小于三个点的时候
			mHeaderText.setTextColor(Color.parseColor("#ff0000"));
			mHeaderText.setText(getResources().getString(stage.headerMessage,
					LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
			mHeaderText.startAnimation(mShakeAnim);

		} else {
			// 多于三个点的时候，设置标题文字
			mHeaderText.setText(stage.headerMessage);
			mHeaderText.setTextColor(Color.parseColor("#333333"));
		}

		// if (stage.leftMode == LeftButtonMode.Gone) {
		// mFooterLeftButton.setVisibility(View.GONE);
		// } else {
		// mFooterLeftButton.setVisibility(View.VISIBLE);
		// mFooterLeftButton.setText(stage.leftMode.text);
		// mFooterLeftButton.setEnabled(stage.leftMode.enabled);
		// }
		//
		// mFooterRightButton.setText(R.string.lockpattern_confirm_button_text);
		// mFooterRightButton.setEnabled(true);

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
			mHeaderText.setText("绘制解锁图案");
			mHeaderText.setTextColor(Color.parseColor("#333333"));
			break;
		case ChoiceTooShort:
			// 少于两个点的时候
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case FirstChoiceValid:
			mHeaderText.setText("绘制解锁图案");
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

	/**
	 * 
	 * TODO<清空九宫格中的轨迹>
	 * 
	 * @throw
	 * @return void
	 */
	private void postClearPatternRunnable() {
		// 清空九宫格中画的线
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		// 间隔两秒
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	/**
	 * 
	 * TODO<保存九宫格中的数据>
	 * 
	 * @throw
	 * @return void
	 */
	private void saveChosenPatternAndFinish() {
		app.getLockPatternUtils().saveLockPattern(mChosenPattern);
		LoginManager lm = LoginManager
				.getInstance(CreateGesturePasswordActivity.this);
		shopsid = lm.getLoginId();
		// 得到用户名和将手势密码转换成string类型
		String patternToString = LockPatternUtils
				.patternToString(mChosenPattern);
		// 把手势密码 存储到数据库中
		// dao.insertData(patternToString);
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(),
				ChangeOldPasswordActivity.class);
		intent.putExtra("patternToString", patternToString);
		startActivity(intent);
		finish();
	}
}
