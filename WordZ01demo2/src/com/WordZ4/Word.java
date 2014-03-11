package com.WordZ4;


public class Word {
        private String content;
        private String define;
		private String file;  
		public String fileID;
        public String wordID;   
        private int prio;
        private int mBackgroundRes;
      
    	public Word(String wordID,String content,String background_color, String define ,String file,String fileID, int prio){
    		this.content = content;
    		this.define = define;
    		this.file=file;
    		this.fileID= fileID;
    		this.prio = prio;
    		this.wordID= wordID;
    		this.mBackgroundRes=Integer.parseInt(background_color);
    	}
        
               
        public void setBackgroundRes(int background){
        	
        	
        	
           this.mBackgroundRes=	background;
        }
        
        public int getBackgroundRes(){
            return mBackgroundRes;
         }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        
        
        public String getDefine() {
            return define;
        }

        public void setDefine(String define) {
            this.define = define;
        }
        
        public String getfile() {
            return file;
        }

        public void setInfile(String file) {
            this.file = file;
        }
        
        public int getPrio() {
            return prio;
        }

        public void setPrio(int prio) {
            this.prio = prio;
        }

  
}