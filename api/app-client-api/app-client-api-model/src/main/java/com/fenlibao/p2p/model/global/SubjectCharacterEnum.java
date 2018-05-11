package com.fenlibao.p2p.model.global;

/**
 * 主体性质
 */
public enum SubjectCharacterEnum {

	ONE(1, "自然人"),

	TWO(2, "法人"),

	THREE(3, "其他组织机构");

	private int index;

	private String content;

	private SubjectCharacterEnum(int index, String content){
		this.index = index;
		this.content = content;
	}
	
	public static String getSubjectCharacter(int index){
		for(SubjectCharacterEnum subjectCharacter: SubjectCharacterEnum.values()){
			if(subjectCharacter.getIndex() == index){
				return subjectCharacter.getContent();
			}
		}
		return "";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
