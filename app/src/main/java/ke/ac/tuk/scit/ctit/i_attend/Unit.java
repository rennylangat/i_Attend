package ke.ac.tuk.scit.ctit.i_attend;

public class Unit {
    private String name;
    private int numOfUnits;
    private int thumbnail;

    public Unit(){

    }

    public Unit (String name,int numOfUnits,int thumbnail){
        this.name=name;
        this.numOfUnits=numOfUnits;
        this.thumbnail=thumbnail;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfUnits() {
        return numOfUnits;
    }

    public void setNumOfUnits(int numOfUnits) {
        this.numOfUnits = numOfUnits;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
