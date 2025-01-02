package io.github.singlerr.sgadminui.client.ui;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.TextView;
import io.github.singlerr.sgadminui.client.GameHistory;
import io.github.singlerr.sgadminui.client.GameInfo;
import io.github.singlerr.sgadminui.client.GamePlayer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

@RequiredArgsConstructor
public final class GameInfoUI extends Fragment {

  private final GameInfo info;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           DataSet savedInstanceState) {
    LinearLayout content = new LinearLayout(requireContext());
    content.setOrientation(LinearLayout.HORIZONTAL);
    content.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
    content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));

    LinearLayout gameInfoGroup = new LinearLayout(requireContext());
    gameInfoGroup.setOrientation(LinearLayout.VERTICAL);
    gameInfoGroup.setGravity(Gravity.CENTER_VERTICAL);
    gameInfoGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));

    LinearLayout playerListGroup = new LinearLayout(requireContext());
    playerListGroup.setOrientation(LinearLayout.VERTICAL);
    playerListGroup.setGravity(Gravity.CENTER_VERTICAL);
    playerListGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT));

    gameInfoGroup.addView(createGameInfo(info.getInfo()));
    playerListGroup.addView(createPlayerList(info.getPlayerList()));

    return content;
  }

  private LinearLayout createPlayerInfo(GamePlayer p){
    LinearLayout group = new LinearLayout(requireContext());
    group.setOrientation(LinearLayout.VERTICAL);
    group.setGravity(Gravity.LEFT);

    TextView nameView = new TextView(requireContext());
    nameView.setText(p.getDisplayName());
    TextView roleView = new TextView(requireContext());
    roleView.setText(p.getRole());

    return group;
  }

  private LinearLayout createGameInfo(GameHistory game){
    LinearLayout group = new LinearLayout(requireContext());
    group.setOrientation(LinearLayout.VERTICAL);
    group.setGravity(Gravity.LEFT);

    TextView nameView = new TextView(requireContext());
    nameView.setText(game.getId());
    TextView playerCountView = new TextView(requireContext());
    playerCountView.setText(MessageFormatter.basicArrayFormat("{}/{} (생존자 수/플레이어 수)", new Object[]{game.getSurvivorsCount(), game.getPlayerCount()}));
    return group;
  }

  private ScrollView createPlayerList(GameInfo.PlayerList list){
    ScrollView view = new ScrollView(requireContext());
    for (GamePlayer p : list.getPlayerList()) {
      LinearLayout playerView = createPlayerInfo(p);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      params.setMargins(view.dp(3), view.dp(5), view.dp(3), view.dp(5));
      playerView.addView(playerView, params);
    }
    return view;
  }

  private ScrollView createGameList(List<GameHistory> games){
    ScrollView view = new ScrollView(requireContext());
    for (GameHistory game: games) {
      LinearLayout playerView = createGameInfo(game);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      params.setMargins(view.dp(3), view.dp(5), view.dp(3), view.dp(5));
      playerView.addView(playerView, params);
    }
    return view;
  }



  private LinearLayout createGameInfo(GameInfo.Game game){
    LinearLayout group = new LinearLayout(requireContext());
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.setMargins(group.dp(3), group.dp(5), group.dp(3), group.dp(5));
    TextView v = new TextView(requireContext());
    v.setText("현재 진행중인 게임: " + game.getCurrentGameId());
    group.addView(v, params);

    ScrollView gameList = createGameList(game.getGames());
    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.setMargins(group.dp(3), group.dp(10), group.dp(3), group.dp(3));
    group.addView(gameList, params);
    return group;
  }
}
