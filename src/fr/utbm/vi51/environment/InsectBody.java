package fr.utbm.vi51.environment;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 *
 */
public class InsectBody extends Body {

	private final static int MAX_HEALTH = 10;
	
	private final InsectBodyType type;
	
    private boolean 	isAlive;
    private int 		hunger;
    private Side 		side;
    private WorldObject carriedObject;
    
    private int			currentHealthPoints;
    private int 		maxHealthPoints;
    private int 		attackPower;

    public InsectBody(String texture, Point3D position, int speed, Side side) {
        super(texture, position, speed);
        this.side = side;
        this.hunger = 0;
        this.isAlive = true;
        this.attackPower = 2;
        // Use the body path to determine the function of the insect
	    if(texture.startsWith("img/Ants/warrior")) { //$NON-NLS-1$
	    	this.type = InsectBodyType.WARRIOR;
	        this.currentHealthPoints = this.maxHealthPoints = InsectBody.MAX_HEALTH;
	    }
	    else if(texture.startsWith("img/Ants/worker")) { //$NON-NLS-1$
	    	this.type = InsectBodyType.WORKER;
	        this.currentHealthPoints = this.maxHealthPoints = InsectBody.MAX_HEALTH/2;
	    }
	    else if(texture.startsWith("img/Ants/queen")) { //$NON-NLS-1$
	    	this.type = InsectBodyType.QUEEN;
	        this.currentHealthPoints = this.maxHealthPoints = InsectBody.MAX_HEALTH*3;
	    }
	    else {
	    	this.type = InsectBodyType.WORKER;
	    }
	    this.side.addMember(this.type);
    }

    public Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    protected void die() {
    	if(this.isAlive == false) {
    		return;
    	}
        this.isAlive = false;
        this.side.removeMember(this.type);
    }

    public int getHunger() {
        return this.hunger;
    }

    protected void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public boolean isHungry() {
        return this.hunger > Consts.MAXHUNGER * 0.8;
    }

	public int getCurrentHealth() {
		return this.currentHealthPoints;
	}
	
	public int getMaxHealth() {
		return this.maxHealthPoints;
	}
	
	public void hit(int amountOfDamage) {
		if(amountOfDamage <= 0) {
			return;
		}
		this.currentHealthPoints = Math.max(0,this.currentHealthPoints - amountOfDamage);
		
		if(this.currentHealthPoints == 0) {
			die();
		}
	}
	
	public int generateHitAmount() {
		return (int) Math.floor(Math.random() * this.attackPower);
	}

    public WorldObject getCarriedObject() {
        return this.carriedObject;
    }

    protected void setCarriedObject(WorldObject carriedObject) {
        this.carriedObject = carriedObject;
    }

	public InsectBodyType getType() {
		return this.type;
	}

}
