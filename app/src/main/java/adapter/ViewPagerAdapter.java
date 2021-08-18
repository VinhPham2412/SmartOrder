package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.Waiter.CommunicationFragment;
import Fragments.Waiter.CookedOrderFragment;
import Fragments.Waiter.WaitingBillFragment;
import Fragments.Waiter.OrderFragment;
import Fragments.Waiter.WaiterOrderFragment;

/**
 * ViewPageAdapter for waiter's main screen
 * 
 */
public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                return new OrderFragment();
            case 1:
                return new WaitingBillFragment();
            case 2:
                return new CookedOrderFragment();
            case 3:
                return new CommunicationFragment();
            default:
                return new WaiterOrderFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}
