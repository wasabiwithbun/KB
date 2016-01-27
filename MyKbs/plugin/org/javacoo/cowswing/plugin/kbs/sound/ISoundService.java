package org.javacoo.cowswing.plugin.kbs.sound;

import javax.swing.text.JTextComponent;

/**
 * 声音服务
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * @author DuanYong
 * @since 2015-11-3下午8:14:56
 * @version 1.0
 */
public interface ISoundService {
	void startSoundToText(JTextComponent cmp);
	void stopSoundToText();
	void startTextToSound();
	void stopTextToSound();
}
