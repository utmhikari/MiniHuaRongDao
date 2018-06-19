package main;

/**
 * 华容道谜题类
 */

public class HuaRongDaoPuzzle {

    private String name;  // 谜题名称
    private String description;  // 谜题描述
    private HuaRongDaoCharacter character;  // 谜题角色
    private boolean isSet;  // 谜题是否设置bool


    /**
     * 构造函数（空谜题）
     * @param name 谜题名称
     */
    public HuaRongDaoPuzzle(String name) {
        this.name = name;
        this.description = "";
        character = new HuaRongDaoCharacter();
        isSet = false;

    }

    /**
     * 构造函数（有谜题）
     * @param name 谜题名称
     * @param character 角色
     */
    public HuaRongDaoPuzzle(String name, HuaRongDaoCharacter character) {
        this.name = name;
        this.description = "";
        if(character.isValid()) {
            this.character = character;
            isSet = true;
        } else {
            System.out.println("Error occured in character settings of puzzle: " + name);
            this.character = new HuaRongDaoCharacter();
            isSet = false;
        }
    }

    /**
     * 设置谜题名称
     * @param name 新谜题名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取谜题描述
     * @return 谜题描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置新谜题描述
     * @param description 新谜题描述
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * 获取谜题名称
     * @return 谜题名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色
     * @param character 新华容道角色
     */
    public void setCharacter(HuaRongDaoCharacter character) {
        if(character.isValid()) {
            this.character.getCharMap().clear();
            this.character = character;
            isSet = true;
        } else {
            System.out.println("Error occured in character settings of puzzle: " + name);
        }
    }

    /**
     * 获取角色
     * @return 华容道角色
     */
    public HuaRongDaoCharacter getCharacter() {
        return character;
    }

    /**
     * 是否设置角色谜题
     * @return 谜题是否设置bool
     */
    public boolean isSet() {
        return isSet;
    }

}
