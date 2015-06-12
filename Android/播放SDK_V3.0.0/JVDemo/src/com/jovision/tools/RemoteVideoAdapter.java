
package com.jovision.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jovision.beans.RemoteVideo;
import com.jovision.demo.R;

import java.util.ArrayList;

public class RemoteVideoAdapter extends BaseAdapter {

    ArrayList<RemoteVideo> videoList = new ArrayList<RemoteVideo>();
    public Context mContext = null;
    public LayoutInflater inflater;

    public RemoteVideoAdapter(Context con) {
        mContext = con;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<RemoteVideo> list) {
        videoList = list;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (null != videoList && 0 != videoList.size()) {
            size = videoList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        RemoteVideo rv = new RemoteVideo();
        if (null != videoList && 0 != videoList.size()
                && position < videoList.size()) {
            rv = videoList.get(position);
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.remotevideo_item, null);
            viewHolder = new ViewHolder();
            viewHolder.videoDate = (TextView) convertView
                    .findViewById(R.id.videodate);
            viewHolder.videoDisk = (TextView) convertView
                    .findViewById(R.id.videodisk);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (null != videoList && 0 != videoList.size()
                && position < videoList.size()) {

            // A：alarm报警录像；M：motion移动侦测；T:定时录像 N：normal手动录像
            if ("A".equalsIgnoreCase(videoList.get(position).remoteKind)) {
                viewHolder.videoDate.setText(videoList.get(position).remoteDate
                        + "-"
                        + mContext.getResources().getString(
                                R.string.video_alarm));
            } else if ("M".equalsIgnoreCase(videoList.get(position).remoteKind)) {
                viewHolder.videoDate.setText(videoList.get(position).remoteDate
                        + "-"
                        + mContext.getResources().getString(
                                R.string.video_motion));
            } else if ("T".equalsIgnoreCase(videoList.get(position).remoteKind)) {
                viewHolder.videoDate.setText(videoList.get(position).remoteDate
                        + "-"
                        + mContext.getResources()
                                .getString(R.string.video_time));
            } else {
                viewHolder.videoDate.setText(videoList.get(position).remoteDate
                        + "-"
                        + mContext.getResources().getString(
                                R.string.video_normal));
            }

            viewHolder.videoDisk.setText(videoList.get(position).remoteDisk);
        }
        return convertView;
    }

    class ViewHolder {
        TextView videoDate;
        TextView videoDisk;
    }
}
