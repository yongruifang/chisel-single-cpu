int main()
{
  int x = 12;
  int y = 20;
  // 求x和y的最大公约数
  int xl = x;
  int sm = y;
  if( xl < sm) {
    xl = y;
    sm = x;
  }
  while( xl != sm ) {
    int diff = xl - sm;
    if( sm > diff) {
      xl = sm;
      sm = diff;
    }else {
      xl = diff;
    }
  }
  return 0;
}
