package org.javacoo.cowswing.plugin.kbs.sound.impl;

import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.plugin.kbs.sound.ISoundService;
import org.javacoo.cowswing.plugin.kbs.utils.JsonParser;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;

public class SoundServiceImpl implements ISoundService{
	protected Logger logger = Logger.getLogger(this.getClass());
	// 语音听写对象
	private SpeechRecognizer mIat;
	/**文字输出组件*/
	private  JTextComponent outputCmp;
	private void init(){
		// 初始化
		SpeechUtility.createUtility("appid=53b93722");
		// 初始化听写对象
		mIat = SpeechRecognizer.createRecognizer();
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "./iflytek_iat.pcm");
	}
	
	
	/**
	 * 听写监听器
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			logger.info( "onBeginOfSpeech enter" );
			//((JLabel) jbtnRecognizer.getComponent(0)).setText("听写中...");
			//jbtnRecognizer.setEnabled(false);
		}

		@Override
		public void onEndOfSpeech() {
			logger.info( "onEndOfSpeech enter" );
		}

		/**
		 * 获取听写结果. 获取RecognizerResult类型的识别结果，并对结果进行累加，显示到Area里
		 */
		@Override
		public void onResult(RecognizerResult results, boolean islast) {
			logger.info( "onResult enter" );
			
			String text = JsonParser.parseIatResult(results.getResultString());
			
			outputCmp.setText(text);	
//			resultArea.append(text);

//			if( islast ){
//				iatSpeechInitUI();
//			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			logger.info( "onVolumeChanged enter" );
			if (volume == 0)
				volume = 1;
			else if (volume >= 6)
				volume = 6;
			//labelWav.setIcon(new ImageIcon("res/mic_0" + volume + ".png"));
		}

		@Override
		public void onError(SpeechError error) {
			logger.info( "onError enter" );
			if (null != error){
				logger.info("onError Code：" + error.getErrorCode());
				outputCmp.setText( error.getErrorDescription(true) );
				//iatSpeechInitUI();
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			logger.info( "onEvent enter" );
		}
	};
	@Override
	public void startSoundToText(JTextComponent cmp) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stopSoundToText() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void startTextToSound() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stopTextToSound() {
		// TODO Auto-generated method stub
		
	}

}
