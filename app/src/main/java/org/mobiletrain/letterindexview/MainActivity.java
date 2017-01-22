package org.mobiletrain.letterindexview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<UserEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.lv);
        initData();
        final MyAdapter adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);
        TextView textViewDialog = (TextView) findViewById(R.id.show_letter_dialog);
        final LetterIndexView letterIndexView = (LetterIndexView) findViewById(R.id.letter_index_view);
        letterIndexView.setTextViewDialog(textViewDialog);
        //实现LetterIndexView中的接口，当手指在右边滑动时，ListView能够自动滚动
        letterIndexView.setUpdateListView(new LetterIndexView.UpdateListView() {
            @Override
            public void updateListView(int currentChar) {
                int positionForSection = adapter.getPositionForSection(currentChar);
                listView.setSelection(positionForSection);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //当ListView滚动时，更新右边滑动控件中字母的颜色
                int sectionForPosition = adapter.getSectionForPosition(firstVisibleItem);
                letterIndexView.updateLetterIndexView(sectionForPosition);
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        String[] allUserNames = getResources().getStringArray(R.array.arrUsernames);
        for (int i = 0; i < allUserNames.length; i++) {
            UserEntity userEntity = new UserEntity();
            String userName = allUserNames[i];
            userEntity.setUsername(userName);
            String pinyin = ChineseToPinyinHelper.getInstance().getPinyin(userName);
            userEntity.setPinyin(pinyin);
            String s = pinyin.substring(0, 1).toUpperCase();
            if (s.matches("[A-Z]")) {
                userEntity.setFirstLetter(s);
            } else {
                userEntity.setFirstLetter("#");
            }
            list.add(userEntity);
        }
        //对ListView中的数据进行排序
        Collections.sort(list, new Comparator<UserEntity>() {
            //当lhs小于、等于、大于rhs时，分别返回负数，0，正数
            @Override
            public int compare(UserEntity lhs, UserEntity rhs) {
                if (lhs.getFirstLetter().contains("#")) {
                    return 1;
                } else if (rhs.getFirstLetter().contains("#")) {
                    return -1;
                } else {
                    return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
                }
            }
        });
    }
}
