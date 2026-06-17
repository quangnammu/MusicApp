package com.huynhquangnam.musicapp.Data;

import com.huynhquangnam.musicapp.Element.Song;

import java.util.ArrayList;

public class ListSong {
    private static ArrayList<Song> listSong;

    public  static ArrayList<Song> getListSong(){
        if(listSong == null){
            listSong = new ArrayList<>();
            listSong.add(new Song("Christmas (Baby Please Come Home)",
                    "Michael Bublé",
                    "https://vnso-zn-15-tf-mp3-320s1-zmp3.zadn.vn/7baf71a8c1ec28b271fd/7990039449912688277?authen=exp=1574757297~acl=/7baf71a8c1ec28b271fd/*~hmac=d479448a72ed63fc8ec8acb2c1230d9a",
                    "https://photo-resize-zmp3.zadn.vn/w480_r1x1_jpeg/covers/3/9/39f7d0ca29707a13cb2bfcebcbba39c0_1319341977.jpg",
                    "Đang cập nhật"));
            listSong.add(new Song("cha",
                    "Michael Bublé",
                    "https://vnso-zn-15-tf-mp3-320s1-zmp3.zadn.vn/7baf71a8c1ec28b271fd/7990039449912688277?authen=exp=1574757297~acl=/7baf71a8c1ec28b271fd/*~hmac=d479448a72ed63fc8ec8acb2c1230d9a",
                    "https://photo-resize-zmp3.zadn.vn/w480_r1x1_jpeg/covers/3/9/39f7d0ca29707a13cb2bfcebcbba39c0_1319341977.jpg",
                    "Đang cập nhật"));
            listSong.add(new Song("cha cha cha",
                    "Michael Bublé",
                    "https://vnso-zn-15-tf-mp3-320s1-zmp3.zadn.vn/7baf71a8c1ec28b271fd/7990039449912688277?authen=exp=1574757297~acl=/7baf71a8c1ec28b271fd/*~hmac=d479448a72ed63fc8ec8acb2c1230d9a",
                    "https://photo-resize-zmp3.zadn.vn/w480_r1x1_jpeg/covers/3/9/39f7d0ca29707a13cb2bfcebcbba39c0_1319341977.jpg",
                    "Đang cập nhật"));
        }
        return listSong;
    }
}
