package com.developer.andersonmiller.opencv;


public class FavEntry {

        private int _id;
        private String Color;
        private String hexValue;
        private String rgb;
        private String hsv;
        private String cmyk;
        private String lab;

        public FavEntry(int _id,String Color, String hexValue, String rgb, String hsv, String cmyk, String lab){
            this.Color = Color;
            this.hexValue = hexValue;
            this.rgb = rgb;
            this.hsv = hsv;
            this.cmyk=cmyk;
            this.lab = lab;
             this._id =_id;
        }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getColor() {
            return Color;
        }

        public void setColor(String Color){
            this.Color=Color;
        }

        public String getHexValue(){
            return hexValue;
        }

        public void setHexValue(String hexValue){
            this.hexValue=hexValue;
        }

        public String getRgb(){
            return rgb;
        }

        public void setRgb(String rgb){
            this.rgb=rgb;
        }

        public String getHSV(){
            return hsv;
        }

        public void setHSV(String hsv){
            this.hsv=hsv;
        }

        public String getCMYK(){
            return cmyk;
        }

        public void setCMYK(String cmyk){
            this.cmyk=cmyk;
        }

        public String getLab(){
            return lab;
        }
        public void setLab(String lab){

            this.lab = lab;
        }
    }
