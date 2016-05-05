requestWindowFeature(Window.FEATURE_NO_TITLE);
LinearLayout ll = new LinearLayout(this);

NavigationBar nb = new NavigationBar(this);
nb.setLeftBarButton("Like");
nb.setRightBarButton("Details");
nb.setBarTitle("Block);
NavigationBar.NavigationBarListener nbl = new NavigationBar.NavigationBarListener() {

    @Override
    public void OnNavigationButtonClick(int which) {
        // do stuff here
    }
};
nb.setNavigationBarListener(nbl);
ll.addView(nb);
setContentView(ll);
