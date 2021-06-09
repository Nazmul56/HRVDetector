package com.example.android.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Contract;
import java.util.HashMap;
import java.util.Map;

/**
 */
@SuppressWarnings("StatementWithEmptyBody")
public class NavigationViewModel extends ViewModel {

    private static String TAG = NavigationViewModel.class.getSimpleName();
    private static NavigationViewModel instance;
    private static Map<String, String> stickerUrlMap = new HashMap<>();
    private String myUsername;
    private boolean isNavigationActivityRunning = false;
    private MutableLiveData<Boolean> mutableFlagOnlineStatusOfCallHistoryAdapter = new MutableLiveData<>();

    private MutableLiveData<Boolean> isContactSyncing = new MutableLiveData<Boolean>();

    /**
     * Put sticker url.
     *
     * @param packId    the pack id
     * @param stickerId the sticker id
     * @param url       the url
     */
    public static void putStickerUrl(String packId, String stickerId, String url) {
        stickerUrlMap.put(packId + "_" + stickerId, url);
    }

    /**
     * Gets sticker url.
     *
     * @param packId    the pack id
     * @param stickerId the sticker id
     * @return the sticker url
     */
    public static String getStickerUrl(String packId, String stickerId) {
        return stickerUrlMap.get(packId + "_" + stickerId);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static NavigationViewModel getInstance() {
        return instance;
    }

    public void setIsNavigationActivityRunning(boolean isNavigationActivityRunning){
        this.isNavigationActivityRunning = isNavigationActivityRunning;
    }

    public boolean getIsNavigationActivityRunning(){
        return isNavigationActivityRunning;
    }

    /**
     * notify to update CallHistory Adapter
     *
     * @return the call history live list
     */
    public LiveData<Boolean> getOnlineStatusChangedListener() {
        return mutableFlagOnlineStatusOfCallHistoryAdapter;
    }

    public void setOnlineStatusChangedListener() {
        mutableFlagOnlineStatusOfCallHistoryAdapter.postValue(true);
    }

    /**
     * getLiveContactSync
     * @return boolean
     */
    public MutableLiveData<Boolean> getLiveContactListSync() {
        return isContactSyncing;
    }

    /**
     * Sets contact Sync live data.
     */
    public void setLiveContactListSync(boolean isContactSyncing) {
        this.isContactSyncing.postValue(isContactSyncing);
    }
}
