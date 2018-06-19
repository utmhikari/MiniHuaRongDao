package main;

import java.util.*;

/**
 * 华容道解题类
 * 华容道位置（坐标）图：
 * 00   10  20  30
 * 01   11  21  31
 * 02   12  22  32
 * 03   13  23  33
 * 04   14  24  34
 *
 */
public class HuaRongDaoSolver {

    private static HashSet<Integer> statusSet = new HashSet<>();  // 终极保险，存储地图状态的编码
    private static HashMap<Integer, ArrayList<String>> charNameMap = new HashMap<>();  // 按大小分组的角色图（有序，编码用）
    private static HashMap<Integer, ArrayList<String>> moveMap = new HashMap<>();  // 存储所有移动情况
    private static int[] prime = {11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};

    /**
     * 华容道谜题解谜
     * @param puzzle 华容道谜题
     */
    public static void solve(HuaRongDaoPuzzle puzzle) {
        // 检查谜题是否已设置
        if(!puzzle.isSet()) {
            System.out.println("\n谜题: " + puzzle.getName() + " —— 未设置!\n");
            return;
        }
        // 清空状态集合、按大小分组的角色图以及移动情况
        if(!statusSet.isEmpty()) {
            statusSet.clear();
        }
        if(!charNameMap.isEmpty()) {
            charNameMap.clear();
        }
        if(!moveMap.isEmpty()) {
            moveMap.clear();
        }
        long start = System.currentTimeMillis();
        if(solve(puzzle.getCharacter())) {
            long end = System.currentTimeMillis();
            System.out.println("\n谜题: " + puzzle.getName() + " —— 已解决!");
            System.out.println("用时: " + ((double)(end-start))/1000 + "秒！\n");
        } else {
            long end = System.currentTimeMillis();
            System.out.println("\n谜题: " + puzzle.getName() + " —— 无法解决!");
            System.out.println("用时: " + ((double)(end-start))/1000 + "秒！\n");
        }
    }

    /**
     * 华容道角色解谜
     * @param character 华容道角色
     */
    private static boolean solve(HuaRongDaoCharacter character) {
        if(character.isValid()) {
            return solve(character.getCharMap(), character.getMainCharacter());
        } else {
            System.out.println("Error! The character map is not valid!");
            return false;
        }
    }

    /**
     * 华容道角色图解谜
     * @param charMap 华容道角色图
     * @param mainCharacter 主角名称
     */
    private static boolean solve(HashMap<String, int[]> charMap, String mainCharacter) {
        // 首先将角色按size分组
        charMap.forEach((charName, property)->{
           int size = property[0] * 10 + property[1];
           if(!charNameMap.keySet().contains(size)) {
               charNameMap.put(size, new ArrayList<String>(){{ add(charName); }});
           } else {
               charNameMap.get(size).add(charName);
           }
        });
        LinkedList<HashMap<String, int[]>> charMapList = new LinkedList<>();  // 角色图列表
        charMapList.addLast(charMap);  // 添加至角色图
        int stepCount = 0;  // 步数统计
        statusSet.add(encodeCharMap(charMap, false));  // 添加角色图编码
        statusSet.add(encodeCharMap(charMap, true));  // 添加角色图编码（镜像）
        return solve(charMapList, stepCount, mainCharacter);
    }

    /**
     * 华容道角色图解谜（BFS）
     * @param charMapList 华容道角色图列表
     * @param stepCount 步数统计
     * @param mainCharacter 主角名称
     */
    private static boolean solve(LinkedList<HashMap<String, int[]>> charMapList, int stepCount, String mainCharacter) {
        // 输出当前状态情况
        // System.out.println("StepCount: " + stepCount + ", ListSize: " + charMapList.size() + ", StatusSetSize: " + statusSet.size());
        // 如果角色图列表为空或者统计步数大于200则无解
        if (charMapList.isEmpty() || stepCount > 200) {
            System.out.println("\nCannot be solved!");
            return false;
        }
        // 首先判断主角是否已经到达终点
        Iterator<HashMap<String, int[]>> iter = charMapList.iterator();  // 链表迭代器
        int iterCount = 0; // 迭代器遍历计数
        while(iter.hasNext()) {
            HashMap<String, int[]> charMap = iter.next();
            int[] mainCharacterProperty = charMap.get(mainCharacter);
            if(Arrays.equals(mainCharacterProperty, new int[]{2, 2, 1, 3})) {
                // 输出步数结果
                System.out.println("\nThe puzzle is solved! Total steps: " + stepCount);
                // 输出移动步骤
                System.out.println("\nThe moves are:");
                ArrayList<String> reverseMoves = new ArrayList<>();
                StringBuilder moveOutput = new StringBuilder();
                int nextIndex = iterCount;
                for(int i = stepCount - 1; i >= 0; i--) {
                    ArrayList<String> moves = moveMap.get(i);
                    String[] moveStrSplit = moves.get(nextIndex).split("_");
                    reverseMoves.add(moveStrSplit[0]);
                    nextIndex = Integer.parseInt(moveStrSplit[1]);
                }
                HashMap<String, String> dirMap = new HashMap<String, String>() {{
                    put("u", "上");
                    put("d", "下");
                    put("l", "左");
                    put("r", "右");
                }};
                int step = 1;
                for(int i = 1; i <= reverseMoves.size(); i++) {
                    String[] moveCharDir = reverseMoves.get(reverseMoves.size() - i).split(":");
                    moveOutput.append(step++);
                    moveOutput.append(": ");
                    moveOutput.append(moveCharDir[0]);
                    moveOutput.append("向");
                    moveOutput.append(dirMap.get(moveCharDir[1]));
                    moveOutput.append("\t");
                    if(i % 10 == 0) {
                        moveOutput.append("\n");
                    }
                }
                System.out.println(moveOutput.toString());
                return true;
            }
            iterCount++;
        }
        // 如果主角未到达终点，则进行后续解谜步骤
        iter = charMapList.iterator();  // 重新初始化迭代器
        iterCount = 0;  // 重新初始化迭代器计数
        LinkedList<HashMap<String, int[]>> newCharMapList = new LinkedList<>();  // 新角色图列表
        // 如果没解完，访问原角色图
        while (iter.hasNext()) {
            HashMap<String, int[]> charMap = iter.next();  // 读取角色表
            HashSet<Integer> blankSet = getBlankSet(charMap);  // 读取空白位置集合
            ArrayList<Integer> posList = new ArrayList<>(); // 角色移动占据位置列表
            final int itc = iterCount;
            // 遍历角色表，移动角色，为列表添加新的角色图
            charMap.forEach((charName, property)->{
                addStatus(charMap, charName, property, 'u', posList, blankSet, newCharMapList, stepCount, itc);
                addStatus(charMap, charName, property, 'd', posList, blankSet, newCharMapList, stepCount, itc);
                addStatus(charMap, charName, property, 'l', posList, blankSet, newCharMapList, stepCount, itc);
                addStatus(charMap, charName, property, 'r', posList, blankSet, newCharMapList, stepCount, itc);
            });
            iterCount++;  // 迭代器计数+1
        }
        charMapList.clear();  // 删除当前角色图
        charMapList = newCharMapList;  // 更新角色图
        // 返回后续的角色图解谜结果
        return solve(charMapList, stepCount + 1, mainCharacter);
    }

    /**
     * 获取空白位置集合
     * @param charMap 角色图
     * @return 空白位置集合
     */
    private static HashSet<Integer> getBlankSet(HashMap<String, int[]> charMap) {
        HashSet<Integer> blankSet = new HashSet<>();
        boolean[][] gameArena = new boolean[4][5];
        charMap.forEach((charName, property)->{
            for(int i = 0; i < property[0]; i++) {
                for(int j = 0; j < property[1]; j++) {
                    gameArena[property[2]+i][property[3]+j] = true;
                }
            }
        });
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 5; j++) {
                if(!gameArena[i][j]) {
                    blankSet.add(parseCoordToPos(i, j));
                }
            }
        }
        return blankSet;
    }

    /**
     * 获取移动后的角色图
     * @param charMap 原先角色图
     * @param charName 角色名称
     * @param moveType 移动方式（u、d、l、r，上下左右）
     * @return 移动后的角色图
     */
    private static HashMap<String, int[]> getMovedMap(HashMap<String, int[]> charMap, String charName, char moveType) {
        HashMap<String, int[]> movedMap = new HashMap<>(charMap);
        int[] charProperty = movedMap.get(charName);
        switch (moveType) {
            case 'u':
                movedMap.put(charName, new int[]{charProperty[0], charProperty[1], charProperty[2], charProperty[3]-1});
                break;
            case 'd':
                movedMap.put(charName, new int[]{charProperty[0], charProperty[1], charProperty[2], charProperty[3]+1});
                break;
            case 'l':
                movedMap.put(charName, new int[]{charProperty[0], charProperty[1], charProperty[2]-1, charProperty[3]});
                break;
            case 'r':
                movedMap.put(charName, new int[]{charProperty[0], charProperty[1], charProperty[2]+1, charProperty[3]});
                break;
            default:
                break;
        }
        return movedMap;
    }


    /**
     * 添加新状态
     * @param charMap 当前角色图
     * @param charName 当前角色名称
     * @param property 当前角色属性
     * @param moveType 移动方式
     * @param posList 记录角色移动后占据位置的列表
     * @param blankSet 空白位置列表
     * @param newCharMapList 新角色图列表
     * @param stepCount 步数计数
     * @param iterCount 迭代器计数（上一次索引）
     */
    private static void addStatus(HashMap<String, int[]> charMap, String charName, int[] property,
                                  char moveType, ArrayList<Integer> posList, HashSet<Integer> blankSet,
                                  LinkedList<HashMap<String, int[]>> newCharMapList, int stepCount, int iterCount) {
        // 首先将角色移动一下后所占据位置加入列表
        switch(moveType) {
            case 'u':
                for(int i = 0; i < property[0]; i++) {
                    posList.add(parseCoordToPos(property[2]+i, property[3]-1));
                }
                break;
            case 'd':
                for(int i = 0; i < property[0]; i++) {
                    posList.add(parseCoordToPos(property[2]+i, property[3]+property[1]));
                }
                break;
            case 'l':
                for(int i = 0; i < property[1]; i++) {
                    posList.add(parseCoordToPos(property[2]-1, property[3]+i));
                }
                break;
            case 'r':
                for(int i = 0; i < property[1]; i++) {
                    posList.add(parseCoordToPos(property[2] + property[0], property[3] + i));
                }
                break;
            default:
                break;
        }
        // 如果空白部分能涵盖所有移动后的占据位置，则说明移动有效
        if(blankSet.containsAll(posList)) {
            HashMap<String, int[]> map = getMovedMap(charMap, charName, moveType);
            int mapCode = encodeCharMap(map, false);
            // 如果移动后出现一个新盘面状态，则将该状态与其镜像状态添加到状态集合中
            if(!statusSet.contains(mapCode)) {
                newCharMapList.addLast(map);
                statusSet.add(mapCode);
                statusSet.add(encodeCharMap(map, true));
                StringBuilder moveStr = new StringBuilder();
                moveStr.append(charName);
                moveStr.append(":");
                moveStr.append(moveType);
                moveStr.append("_");
                moveStr.append(iterCount);
                if(!moveMap.keySet().contains(stepCount)) {
                    moveMap.put(stepCount, new ArrayList<String>(){{ add(moveStr.toString()); }});
                } else {
                    moveMap.get(stepCount).add(moveStr.toString());
                }
            }
        }
        posList.clear();
    }

    /**
     * 将坐标转化为位置数字
     * @param x X坐标
     * @param y Y坐标
     * @return 位置数字（详见位置图）
     */
    private static int parseCoordToPos(int x, int y) {
        return x * 10 + y;
    }

    /**
     * 将角色图编码
     * @param charMap 角色图
     * @return 编码
     */
    private static int encodeCharMap(HashMap<String, int[]> charMap, boolean isMirror) {
        int code = 0;
        int index = 0;
        for(Map.Entry<Integer, ArrayList<String>> entry : charNameMap.entrySet()) {
            int groupLen = 0;
            int tmpCode = 0;
            for(String charName : entry.getValue()) {
                int x = isMirror ? 4 - charMap.get(charName)[2] - charMap.get(charName)[0] : charMap.get(charName)[2];
                int y = charMap.get(charName)[3];
                int posCode = parseCoordToPos(x, y);
                tmpCode += posCode * posCode * posCode * posCode;
            }
            tmpCode = tmpCode * prime[index++] - groupLen * prime[index++];
            code += tmpCode;
        }
        return code;
    }

    /**
     * main函数
     * @param args main运行参数
     */
    public static void main(String args[]) {

        HashMap<String, int[]> charMap_YTWJ = new HashMap<String, int[]>() {{
            put("曹操", new int[]{2, 2, 0, 1});
            put("关羽", new int[]{2, 1, 0, 3});
            put("张飞", new int[]{1, 2, 2, 1});
            put("赵云", new int[]{1, 2, 3, 1});
            put("黄忠", new int[]{1, 2, 2, 3});
            put("马超", new int[]{2, 1, 0, 4});
            put("兵1", new int[]{1, 1, 1, 0});
            put("兵2", new int[]{1, 1, 2, 0});
            put("兵3", new int[]{1, 1, 3, 0});
            put("兵4", new int[]{1, 1, 3, 3});
        }};
        HuaRongDaoCharacter character_YTWJ = new HuaRongDaoCharacter(charMap_YTWJ);
        HuaRongDaoPuzzle puzzle_YTWJ= new HuaRongDaoPuzzle("以退为进", character_YTWJ);
        HuaRongDaoSolver.solve(puzzle_YTWJ);

        HashMap<String, int[]> charMap_HDLM = new HashMap<String, int[]>() {{
            put("曹操", new int[]{2, 2, 1, 0});
            put("关羽", new int[]{2, 1, 1, 2});
            put("张飞", new int[]{1, 2, 0, 0});
            put("赵云", new int[]{1, 2, 3, 0});
            put("马超", new int[]{1, 2, 3, 2});
            put("黄忠", new int[]{1, 2, 0, 2});
            put("兵1", new int[]{1, 1, 0, 4});
            put("兵2", new int[]{1, 1, 1, 3});
            put("兵3", new int[]{1, 1, 2, 3});
            put("兵4", new int[]{1, 1, 3, 4});
        }};
        HuaRongDaoCharacter character_HDLM = new HuaRongDaoCharacter(charMap_HDLM);
        HuaRongDaoPuzzle puzzle_HDLM = new HuaRongDaoPuzzle("横刀立马", character_HDLM);
        HuaRongDaoSolver.solve(puzzle_HDLM);
    }
}
