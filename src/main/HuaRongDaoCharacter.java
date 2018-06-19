package main;

import java.util.HashMap;
import java.util.Map;

/**
 * 华容道角色类
 */
public class HuaRongDaoCharacter {

    // 默认角色图
    private static final HashMap<String, int[]> defaultCharMap = new HashMap<String, int[]>() {{
        put("曹操", new int[]{2, 2, 0, 0});
        put("关羽", new int[]{2, 1, 0, 0});
        put("张飞", new int[]{1, 2, 0, 0});
        put("赵云", new int[]{1, 2, 0, 0});
        put("马超", new int[]{1, 2, 0, 0});
        put("黄忠", new int[]{1, 2, 0, 0});
        put("兵1", new int[]{1, 1, 0, 0});
        put("兵2", new int[]{1, 1, 0, 0});
        put("兵3", new int[]{1, 1, 0, 0});
        put("兵4", new int[]{1, 1, 0, 0});
    }};

    private HashMap<String, int[]> charMap;  // 角色图（角色名，角色属性）
    private String mainCharacter;  // 主角（默认：曹操）


    private void outputCharacterProperty(String charName, int[] property) {
        System.out.println("The properties of character " + charName + " is: ");
        System.out.println("Length: " + property[0] + ", Height: " + property[1] + ", X: " + property[2] + ", Y: "
                + property[3]);
    }

    /**
     * 构造函数（空图）
     */
    public HuaRongDaoCharacter() {
        charMap = new HashMap<>(defaultCharMap);
        mainCharacter = "曹操";
    }

    /**
     * 构造函数（有图）
     * @param charMap 角色图
     */
    public HuaRongDaoCharacter(HashMap<String, int[]> charMap) {
        this.charMap = new HashMap<>(charMap);
        mainCharacter = "曹操";
    }

    /**
     * 设置角色
     * @param charName 角色名字
     * @param newProperty 新角色属性（长度为4，分别为Length（长度）、Height（高度）、X（左上角0起）、Y（左上角0起））
     */
    public void setCharacter(String charName, int[] newProperty) {
        if(charName.equals(mainCharacter)) {
            System.out.println("The properties of main character cannot be modified!");
        }
        if(newProperty.length == 4) {
            if (charMap.containsKey(charName)) {
                int[] oriProperty = charMap.get(charName);
                outputCharacterProperty(charName, oriProperty);
                outputCharacterProperty(charName, newProperty);
                charMap.put("charName", newProperty);
                System.out.println("Character " + charName + " reset!");
            } else {
                System.out.println("Character " + charName + " does not exist!");
            }
        } else {
            System.out.println("The array length of parameter \"size\" is not 4!");
        }
    }

    /**
     * 检查角色图是否合法
     * @return 角色图是否合法bool
     */
    public boolean isValid() {
        boolean[][] gameArena = new boolean[4][5];
        for(Map.Entry<String, int[]> entry : charMap.entrySet()) {
            int[] property = entry.getValue();
            for(int i = 0; i < property[0]; i++) {
                for(int j = 0; j < property[1]; j++) {
                    int x = property[2]+i;
                    int y = property[3]+j;
                    if(x < 0 || x > 3 || y < 0 || y > 4 || gameArena[x][y]) {
                        return false;
                    } else {
                        gameArena[x][y] = true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取角色图
     * @return 角色图
     */
    public HashMap<String, int[]> getCharMap() {
        return charMap;
    }

    /**
     * 获取主要角色
     * @return 主要角色
     */
    public String getMainCharacter() {
        return mainCharacter;
    }

    /**
     * 设置主角名称
     * @param mainCharacter 新主角名称
     */
    public void setMainCharacter(String mainCharacter) {
        this.mainCharacter = mainCharacter;
    }
}
