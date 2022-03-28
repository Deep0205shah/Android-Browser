package deep.myappcompany.samplebrowser;

import android.widget.ImageView;

public class websites {
    private int _id;
    private ImageView image;
    private String _url;
    private String _title;

    public websites(){

    }
    public websites(String _url){
        this._url=_url;
    }
    public websites(String _url,String title){
        this._url=_url;
        this._title=title;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }
}
