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
                fragment = new OrderFragment();
                break;
            case 1:
                fragment = new WaitingBillFragment();
                break;
            case 2:
                fragment = new CookedOrderFragment();
                break;
            case 3:
                fragment= new CommunicationFragment();
                break;
            default:
                fragment= new WaiterOrderFragment();
                break;
        }
        return fragment;
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}
