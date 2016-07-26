package com.collecdoo.config;

public class URLConfig {

    public static final String ROOT = "http://api.karaokecuatui.vn/";


    public static String getPrefix() {

        return ROOT + "api/";
    }

    public static String getHome() {
        return getPrefix() + "home/get-home-list-api-new";
    }

    public static String getHomeSearch() {
        return getPrefix() + "home/get-home-search-song-new?search=";
    }

    public static String getNewSongs() {
        return getPrefix() + "get-app-new-song?page=%1$s&pagesize=%2$s&id_member=%3$s";
    }

    public static String getHotSongs() {
        return getPrefix() + "get-app-hot-song?page=%1$s&pagesize=%2$s&id_member=%3$s";
    }

    public static String merchantList() {
        return getPrefix() + "merchant/get-list";
    }

    public static String loginGetTicket() {
        return "http://id.karaokecuatui.vn/cas-server/v1/tickets";
    }

    public static String loginGetServiceTicket() {
        return "http://id.karaokecuatui.vn/cas-server/v1/tickets/%1$s";
    }

    public static String profile() {
        return "http://api.account.karaokecuatui.vn/api/profiles/get-app-profile?u=%1$s";
    }

    public static String register() {

        return "http://api.account.karaokecuatui.vn/api/appregister";

    }

    public static String getType() {
        return getPrefix() + "home/get-home-list-by-category";
    }


    public static String getComposerSingerByCateId() {
        return getPrefix() + "home/get-app-list-cs-by-cate?id_object=%1$s";
    }


    public static String getLike() {
        return getPrefix() + "app-like-obj-by-accid";
    }

    public static String getUnLike() {
        return getPrefix() + "app-unlike-obj-by-accid";
    }

    public static String getPushCategoryLogin() {
        return getPrefix() + "like-list-all-by-accid";
    }

    //------------cua tui-----------
    public static String getSongsByMemberId() {
        return getPrefix() + "home/get-app-list-song-by-member?page=%1$s&pagesize=%2$s&id_member=%3$s";
    }

    public static String getSuggestSongsByMemberId() {
        return getPrefix() + "get-app-suggest-song?id_member=%1$s";
    }

    public static String getComposerByMemberId() {
        return getPrefix() + "home/get-app-list-cs-by-member?page=%1$s&pagesize=%2$s&id_member=%3$s";
    }

    public static String getSuggestComposerByMemberId() {
        return getPrefix() + "get-app-suggest-composer?id_member=%1$s";
    }

    public static String getPlaylistAlbumByMemberId() {
        return getPrefix() + "get-app-ap-by-member?page=%1$s&pagesize=%2$s&id_member=%3$s";
    }

    public static String getSuggestPlaylistByMemberId() {
        return getPrefix() + "get-app-suggest-singer?id_member=%1$s";
    }


    public static String getListSongBySinger() {
        return getPrefix() + "home/get-app-list-song-by-singer?id_object=%1$s&id_member=%2$s&liked=%3$s&page=%4$s" +
                "&pagesize=%5$s";
    }

    public static String getListSongByComposer() {
        return getPrefix() + "home/get-app-list-song-by-composer?id_object=%1$s&id_member=%2$s&liked=%3$s&page=%4$s" +
                "&pagesize=%5$s";
    }

    public static String getListSongByAlbum() {
        return getPrefix() + "get-app-list-song-by-album?id_object=%1$s&id_member=%2$s&liked=%3$s&page=%4$s" +
                "&pagesize=%5$s";
    }

    public static String getListSongByPlaylist() {
        return getPrefix() + "get-app-list-song-by-playlist?id_object=%1$s&id_member=%2$s&liked=%3$s&page=%4$s" +
                "&pagesize=%5$s";
    }

    //------------danh sach--------------
    public static String getAllListSongBySinger() {
        return getPrefix() + "get-home-list-by-singer?page=&pagesize=";
    }

    public static String getAllListSongByComposer() {
        return getPrefix() + "home/get-home-list-by-composer?page=&pagesize=";
    }

    public static String getInitData() {
        return getPrefix() + "get-app-init-data?id_push=%1$s&token=%2$s";
    }

    public static String getUpdateData() {
        return getPrefix() + "get-app-update-data?id_push=%1$s&token=%2$s";
    }


    public static String getSearch(){
        return getPrefix()+"home/app-home-search?search=%1$s&page=1&pagesize=50";
    }

    public static String getHistory(){
        return getPrefix()+"app-history-score?id_member=%1$s&page=%2$s&pagesize=%3$s";
    }

    public static String getUpdateProfileInfo(){
        return getPrefix()+"profiles/app-update-profile";
    }
}
