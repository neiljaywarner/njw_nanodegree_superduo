package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment
{
    public static final int NUM_PAGES = 5;
    private static final String TAG = PagerFragment.class.getSimpleName() ;
    public ViewPager mPagerHandler;
    private myPageAdapter mPagerAdapter;
    private MainScreenFragment[] viewFragments = new MainScreenFragment[NUM_PAGES];
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        mPagerHandler = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new myPageAdapter(getChildFragmentManager());
        if (isRtl()) {
            Log.i(TAG, "isRtl");
            setupViewFragmentsRtl();
        } else {
            Log.i(TAG, "is NOT Rtl");
            setupViewFragments();
        }
        mPagerHandler.setAdapter(mPagerAdapter);
        mPagerHandler.setCurrentItem(MainActivity.current_fragment);
        return rootView;
    }

    private void setupViewFragments() {
        for (int i = 0;i < NUM_PAGES;i++)
        {
            Log.i("NJW", "i=" + i);
            setupViewFragment(i);
        }
    }

    private void setupViewFragmentsRtl() {
        for (int i = NUM_PAGES-1;i >= 0;i--)
        {
            Log.i("NJW", "RTL-i=" + i);

            setupViewFragment(i);
        }
    }

    private boolean isRtl() {
        return getResources().getBoolean(R.bool.rtl);
    }

    private void setupViewFragment(int i) {
        Date fragmentDate;
        if (isRtl()) {
            fragmentDate = new Date(System.currentTimeMillis()+((i+2)*86400000));

        } else {
            fragmentDate = new Date(System.currentTimeMillis()+((i-2)*86400000));

        }
        //TODO: Remove magic number, consider how to get out code smell here.
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        viewFragments[i] = new MainScreenFragment();
        viewFragments[i].setFragmentDate(mformat.format(fragmentDate));
    }


    private class myPageAdapter extends FragmentStatePagerAdapter
    {
        @Override
        public Fragment getItem(int i)
        {
            return viewFragments[i];
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }

        public myPageAdapter(FragmentManager fm)
        {
            super(fm);
        }
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            if (isRtl()) {
                return getDayName(getActivity(),System.currentTimeMillis()+((position+2)*86400000));

            } else {
                return getDayName(getActivity(),System.currentTimeMillis()+((position-2)*86400000));

            }
                    //TODO: Cleanup/fix usage of currentTIme in both places, possibly allows for broken edge case, probably close to midnight
        }
        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.

            Time t = new Time();
            t.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if ( julianDay == currentJulianDay +1 ) {
                return context.getString(R.string.tomorrow);
            }
             else if ( julianDay == currentJulianDay -1)
            {
                return context.getString(R.string.yesterday);
            }
            else
            {
                Time time = new Time();
                time.setToNow();
                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
