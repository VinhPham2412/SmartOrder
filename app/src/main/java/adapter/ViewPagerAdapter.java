package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.Chef.CommunicationFragment;
import Fragments.Waiter.OrderFragment;
import Fragments.Waiter.WaiterOrderFragment;

/**
 * ViewPageAdapter for waiter's main screen
 * 
 */
public class ViewPagerAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity =fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new OrderFragment(fragmentActivity,"new");
                break;
            case 1:
                fragment= new WaiterOrderFragment(fragmentActivity);
                break;
            default:
                fragment= new CommunicationFragment(fragmentActivity);
        }
        return fragment;
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
