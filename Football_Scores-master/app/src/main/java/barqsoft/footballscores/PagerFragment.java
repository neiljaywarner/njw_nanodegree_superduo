package barqsoft.footballscores;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
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
    public static final int MILLIS_IN_A_DAY = 86400000;
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
            fragmentDate = new Date(System.currentTimeMillis()+((i+2)* MILLIS_IN_A_DAY));

        } else {
            fragmentDate = new Date(System.currentTimeMillis()+((i-2)* MILLIS_IN_A_DAY));

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
            long dateInMillis = getDateMillisFromPosition(position);
            return getDayName(getActivity(), dateInMillis);

                    //TODO: Cleanup/fix usage of currentTIme in both places, possibly allows for broken edge case, probably close to midnight
        }

        /**
         * setup Dates RTL or RtL as appropriate, eg if it is Wed M/T/W/R/F or F/R/W/T/M
         * @param position
         * @return date in Millis
         */
        private long getDateMillisFromPosition(int position) {
            Log.i("NJW","pos=" + position);
            boolean isRtl = isRtl();
            isRtl = true;
            Date today = new Date();

            if (isRtl) {
                //Start with 4 and go back to 0 - so the 0th position is +2 days (F) and the 4th position is -2 days (M)
                // if today is Wed.
              //  return System.currentTimeMillis() + ((reversePosition - 2) * MILLIS_IN_A_DAY);
                int reversePosition = Math.abs(position - 4); //since backwards startingindex is 4
              //  Log.i("NJW","pos=" + position + "date=" + dateOfThisPosition);
                Log.i("NJW", "reversePosition=" + reversePosition);
                int relativeDayIndex = reversePosition - 2;
                return System.currentTimeMillis() + ((relativeDayIndex) * MILLIS_IN_A_DAY);

            } else {
                return System.currentTimeMillis() + ((position + 2) * MILLIS_IN_A_DAY);
            }
        }

            //TODO: Use isToday http://developer.android.com/reference/android/text/format/DateUtils.html#isToday(long)
        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.
            Log.i("NJW", DateUtils.formatDateTime(context, dateInMillis, DateUtils.FORMAT_SHOW_DATE));

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

                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
