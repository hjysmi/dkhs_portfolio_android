package me.iwf.photopicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirBean;
import me.iwf.photopicker.event.Selectable;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    protected List<PhotoDirBean> photoDirectories;
    //    protected List<Photo> selectedPhotos;
    public int currentDirectoryIndex = 0;
    private ArrayList<String> selectFilePaths;

    public SelectableAdapter() {
        photoDirectories = new ArrayList<>();
        selectFilePaths = new ArrayList<>();
    }


    //设置选中图片列表
//    public void setSelectPhotos(List<Photo> selectedPhoto) {
//        this.selectedPhotos = selectedPhoto;
//    }

    /**
     * Indicates if the item at position position is selected
     *
     * @param photo Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(Photo photo) {
        if (null != selectFilePaths && null != photo && !TextUtils.isEmpty(photo.getPath())) {
            return getSelectedPhotos().contains(photo.getPath());
        } else {
            return false;
        }
    }


    public List<String> getSelectFilePaths() {
        return selectFilePaths;
    }

    public void setSelectFilePaths(ArrayList<String> selectFilePaths) {
        this.selectFilePaths = selectFilePaths;
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(Photo photo) {
        if (selectFilePaths.contains(photo.getPath())) {
            selectFilePaths.remove(photo.getPath());
        } else {
            selectFilePaths.add(photo.getPath());
        }
    }


    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectFilePaths.clear();
    }


    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectFilePaths.size();
    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<Photo> getCurrentPhotos() {
        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }


    @Override
    public ArrayList<String> getSelectedPhotos() {
        return selectFilePaths;
    }

}