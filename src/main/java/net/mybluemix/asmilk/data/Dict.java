package net.mybluemix.asmilk.data;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Dict implements Serializable {
	
	private static final long serialVersionUID = 3623001288836801516L;
	
	@Key("PinYin")
	private String pinYin;
	
	@Key("PronounceJp")
	private String pronounceJp;
	
	@Key("Tone")
	private String tone;
	
	@Key("Word")
	private String word;
	
	@Key("Comment")
	private String comment;
	
	@Key("Pronounce")
	private String pronounce;
	
	@Key("TtsUrl")
	private String ttsUrl;
	
	@Key("IsAddWord")
	private Boolean isAddWord;
	
	@Key("WordId")
	private Long wordId;
	
	@Key("FromLang")
	private String fromLang;
	
	@Key("ToLang")
	private String toLang;

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getPronounceJp() {
		return pronounceJp;
	}

	public void setPronounceJp(String pronounceJp) {
		this.pronounceJp = pronounceJp;
	}

	public String getTone() {
		return tone;
	}

	public void setTone(String tone) {
		this.tone = tone;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPronounce() {
		return pronounce;
	}

	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	public String getTtsUrl() {
		return ttsUrl;
	}

	public void setTtsUrl(String ttsUrl) {
		this.ttsUrl = ttsUrl;
	}

	public Boolean getIsAddWord() {
		return isAddWord;
	}

	public void setIsAddWord(Boolean isAddWord) {
		this.isAddWord = isAddWord;
	}

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	public String getFromLang() {
		return fromLang;
	}

	public void setFromLang(String fromLang) {
		this.fromLang = fromLang;
	}

	public String getToLang() {
		return toLang;
	}

	public void setToLang(String toLang) {
		this.toLang = toLang;
	}

	@Override
	public String toString() {
		return "Dict [pinYin=" + pinYin + ", pronounceJp=" + pronounceJp + ", tone=" + tone + ", word=" + word
				+ ", comment=" + comment + ", pronounce=" + pronounce + ", ttsUrl=" + ttsUrl + ", isAddWord="
				+ isAddWord + ", wordId=" + wordId + ", fromLang=" + fromLang + ", toLang=" + toLang + "]";
	}

	

}
