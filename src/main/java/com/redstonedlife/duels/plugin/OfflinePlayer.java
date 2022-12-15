package com.redstonedlife.duels.plugin;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.RelativeTeleportFlag;
import net.ess3.nms.refl.ReflUtil;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.*;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

public class OfflinePlayer implements Player {
    private final transient Server server;
    private final transient org.bukkit.OfflinePlayer base;
    private transient Location location = new Location(null, 0, 0, 0, 0, 0);
    private transient World world;
    private boolean allowFlight = false;
    private boolean isFlying = false;
    private String name;

    public OfflinePlayer(final UUID uuid, final Server server) {
        this.server = server;
        this.world = server.getWorlds().get(0);
        this.base = server.getOfflinePlayer(uuid);
        this.name = base.getName();
    }

    public OfflinePlayer(final String name, final Server server) {
        this.server = server;
        this.world = server.getWorlds().get(0);
        this.base = server.getOfflinePlayer(name);
        this.name = name;
    }

    @Override
    public void sendMessage(final String string) {
    }

    @Override
    public @NotNull Component displayName() {
        return null;
    }

    @Override
    public void displayName(@Nullable Component component) {

    }

    @Override
    public String getDisplayName() {
        return base.getName();
    }

    @Override
    public void setDisplayName(final String string) {
    }

    @Override
    public void playerListName(@Nullable Component component) {

    }

    @Override
    public @NotNull Component playerListName() {
        return null;
    }

    @Override
    public @Nullable Component playerListHeader() {
        return null;
    }

    @Override
    public @Nullable Component playerListFooter() {
        return null;
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public int getProtocolVersion() {
        return 0;
    }

    @Override
    public @Nullable InetSocketAddress getVirtualHost() {
        return null;
    }

    @Override
    public void kickPlayer(final String string) {
    }

    @Override
    public void kick() {

    }

    @Override
    public void kick(@Nullable Component component) {

    }

    @Override
    public void kick(@Nullable Component component, PlayerKickEvent.@NotNull Cause cause) {

    }

    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(final ItemStack is) {
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(final double d) {
    }

    @Override
    public double getAbsorptionAmount() {
        return 0;
    }

    @Override
    public void setAbsorptionAmount(double v) {

    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    public Vehicle getVehicle() {
        return null;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location loc) {
        location = loc;
        world = loc.getWorld();
    }

    @Override
    public World getWorld() {
        return world;
    }

    public void teleportTo(final Location lctn) {
        location = lctn;
        world = location.getWorld();
    }

    public void teleportTo(final Entity entity) {
        teleportTo(entity.getLocation());
    }

    @Override
    public int getEntityId() {
        return -1;
    }

    public BlockFace getFacing() {
        return null;
    }

    @Override
    public @NotNull Pose getPose() {
        return null;
    }

    @Override
    public @NotNull SpawnCategory getSpawnCategory() {
        return null;
    }

    @Override
    public boolean performCommand(final String string) {
        return false;
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir(final int i) {
    }

    @Override
    public int getMaximumAir() {
        return 0;
    }

    @Override
    public void setMaximumAir(final int i) {
    }

    @Override
    public int getArrowCooldown() {
        return 0;
    }

    @Override
    public void setArrowCooldown(int i) {

    }

    @Override
    public int getArrowsInBody() {
        return 0;
    }

    @Override
    public void setArrowsInBody(int i) {

    }

    @Override
    public int getBeeStingerCooldown() {
        return 0;
    }

    @Override
    public void setBeeStingerCooldown(int i) {

    }

    @Override
    public int getBeeStingersInBody() {
        return 0;
    }

    @Override
    public void setBeeStingersInBody(int i) {

    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public void setSneaking(final boolean bln) {
    }

    @Override
    public void updateInventory() {
    }

    @Override
    public @Nullable GameMode getPreviousGameMode() {
        return null;
    }

    @Override
    public void chat(final String string) {
    }

    @Override
    public double getEyeHeight() {
        return 0D;
    }

    @Override
    public double getEyeHeight(final boolean bln) {
        return 0D;
    }

    @Override
    public List<Block> getLineOfSight(final Set<Material> mat, final int i) {
        return Collections.emptyList();
    }

    @Override
    public Block getTargetBlock(final Set<Material> mat, final int i) {
        return null;
    }

    @Override
    public @Nullable Block getTargetBlock(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return null;
    }

    @Override
    public @Nullable BlockFace getTargetBlockFace(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return null;
    }

    @Override
    public @Nullable TargetBlockInfo getTargetBlockInfo(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return null;
    }

    @Override
    public @Nullable Entity getTargetEntity(int i, boolean b) {
        return null;
    }

    @Override
    public @Nullable TargetEntityInfo getTargetEntityInfo(int i, boolean b) {
        return null;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(final Set<Material> mat, final int i) {
        return Collections.emptyList();
    }

    @Override
    public @Nullable Block getTargetBlockExact(int i) {
        return null;
    }

    @Override
    public @Nullable Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double v) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(final int i) {
    }

    @Override
    public void setVisualFire(boolean b) {

    }

    @Override
    public boolean isVisualFire() {
        return false;
    }

    @Override
    public int getFreezeTicks() {
        return 0;
    }

    @Override
    public int getMaxFreezeTicks() {
        return 0;
    }

    @Override
    public void setFreezeTicks(int i) {

    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public boolean isFreezeTickingLocked() {
        return false;
    }

    @Override
    public void lockFreezeTicks(boolean b) {

    }

    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void remove() {
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void setPersistent(boolean b) {

    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(final Vector vector) {
    }

    @Override
    public Vector getVelocity() {
        return new Vector(0, 0, 0);
    }

    @Override
    public void setVelocity(final Vector vector) {
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public void damage(final double d) {
    }

    @Override
    public void damage(final double d, final Entity entity) {
    }

    @Override
    public Location getEyeLocation() {
        return null;
    }

    @Override
    public void sendRawMessage(final String string) {
    }

    @Override
    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {

    }

    @Override
    public Location getCompassTarget() {
        return null;
    }

    @Override
    public void setCompassTarget(final Location lctn) {
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(final int i) {
    }

    @Override
    public double getLastDamage() {
        return 0D;
    }

    @Override
    public void setLastDamage(final double d) {
    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(final int i) {
    }

    @Override
    public boolean teleport(final Location lctn) {
        return false;
    }

    @Override
    public boolean teleport(final Entity entity) {
        return false;
    }

    @Override
    public Entity getPassenger() {
        return null;
    }

    @Override
    public boolean setPassenger(final Entity entity) {
        return false;
    }

    @Override
    public List<Entity> getPassengers() {
        return null;
    }

    @Override
    public boolean addPassenger(final Entity passenger) {
        return false;
    }

    @Override
    public boolean removePassenger(final Entity passenger) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean eject() {
        return false;
    }

    @Override
    public void saveData() {
    }

    @Override
    public void loadData() {
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public int getSleepTicks() {
        return 0;
    }

    @Override
    public @Nullable Location getPotentialBedLocation() {
        return null;
    }

    @Override
    public @Nullable FishHook getFishHook() {
        return null;
    }

    @Override
    public boolean sleep(@NotNull Location location, boolean b) {
        return false;
    }

    @Override
    public void wakeup(boolean b) {

    }

    @Override
    public @NotNull Location getBedLocation() {
        return null;
    }

    @Override
    public List<Entity> getNearbyEntities(final double d, final double d1, final double d2) {
        return Collections.emptyList();
    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public float getFallDistance() {
        return 0F;
    }

    @Override
    public void setFallDistance(final float f) {
    }

    @Override
    public boolean isSleepingIgnored() {
        return true;
    }

    @Override
    public void setSleepingIgnored(final boolean bln) {
    }

    @Override
    public void incrementStatistic(final Statistic ststc) {
    }

    @Override
    public void decrementStatistic(final Statistic statistic) throws IllegalArgumentException {
    }

    @Override
    public void incrementStatistic(final Statistic ststc, final int i) {
    }

    @Override
    public void decrementStatistic(final Statistic statistic, final int i) throws IllegalArgumentException {
    }

    @Override
    public void setStatistic(final Statistic statistic, final int i) throws IllegalArgumentException {
    }

    @Override
    public int getStatistic(final Statistic statistic) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(final Statistic ststc, final Material mtrl) {
    }

    @Override
    public void decrementStatistic(final Statistic statistic, final Material material) throws IllegalArgumentException {
    }

    @Override
    public int getStatistic(final Statistic statistic, final Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(final Statistic ststc, final Material mtrl, final int i) {
    }

    @Override
    public void decrementStatistic(final Statistic statistic, final Material material, final int i) throws IllegalArgumentException {
    }

    @Override
    public void setStatistic(final Statistic statistic, final Material material, final int i) throws IllegalArgumentException {
    }

    @Override
    public void incrementStatistic(final Statistic statistic, final EntityType entityType) throws IllegalArgumentException {
    }

    @Override
    public void decrementStatistic(final Statistic statistic, final EntityType entityType) throws IllegalArgumentException {
    }

    @Override
    public int getStatistic(final Statistic statistic, final EntityType entityType) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(final Statistic statistic, final EntityType entityType, final int i) throws IllegalArgumentException {
    }

    @Override
    public void decrementStatistic(final Statistic statistic, final EntityType entityType, final int i) {
    }

    @Override
    public void setStatistic(final Statistic statistic, final EntityType entityType, final int i) {
    }

    @Override
    public void playNote(final Location lctn, final byte b, final byte b1) {
    }

    @Override
    public void sendBlockChange(final Location lctn, final Material mtrl, final byte b) {
    }

    @Override
    public void sendBlockChange(@NotNull Location location, @NotNull BlockData blockData) {

    }

    @Override
    public void sendBlockDamage(@NotNull Location location, float v) {

    }

    @Override
    public void sendMultiBlockChange(@NotNull Map<Location, BlockData> map, boolean b) {

    }

    @Override
    public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {

    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable List<Component> list, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public void setLastDamageCause(final EntityDamageEvent ede) {
    }

    @Override
    public void playEffect(final Location lctn, final Effect effect, final int i) {
    }

    @Override
    public void playNote(final Location lctn, final Instrument i, final Note note) {
    }

    @Override
    public void setPlayerTime(final long l, final boolean bln) {
    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return false;
    }

    @Override
    public void resetPlayerTime() {
    }

    @Override
    public boolean isPermissionSet(final String string) {
        return false;
    }

    @Override
    public boolean isPermissionSet(final Permission prmsn) {
        return false;
    }

    @Override
    public boolean hasPermission(final String string) {
        return false;
    }

    @Override
    public boolean hasPermission(final Permission prmsn) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln, final int i) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final int i) {
        return null;
    }

    @Override
    public void removeAttachment(final PermissionAttachment pa) {
    }

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Collections.emptySet();
    }

    @Override
    public void sendMap(final MapView mv) {
    }

    @Override
    public void sendActionBar(@NotNull String s) {

    }

    @Override
    public void sendActionBar(char c, @NotNull String s) {

    }

    @Override
    public void sendActionBar(@NotNull BaseComponent... baseComponents) {

    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1) {

    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1) {

    }

    @Override
    public void setTitleTimes(int i, int i1, int i2) {

    }

    @Override
    public void setSubtitle(BaseComponent[] baseComponents) {

    }

    @Override
    public void setSubtitle(BaseComponent baseComponent) {

    }

    @Override
    public void showTitle(@Nullable BaseComponent[] baseComponents) {

    }

    @Override
    public void showTitle(@Nullable BaseComponent baseComponent) {

    }

    @Override
    public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i, int i1, int i2) {

    }

    @Override
    public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i, int i1, int i2) {

    }

    @Override
    public void sendTitle(@NotNull Title title) {

    }

    @Override
    public void updateTitle(@NotNull Title title) {

    }

    @Override
    public void hideTitle() {

    }

    @Override
    public GameMode getGameMode() {
        return GameMode.SURVIVAL;
    }

    @Override
    public void setGameMode(final GameMode gm) {
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(final int i) {
    }

    @Override
    public int getTotalExperience() {
        return 0;
    }

    @Override
    public void setTotalExperience(final int i) {
    }

    @Override
    public void sendExperienceChange(float v) {

    }

    @Override
    public void sendExperienceChange(float v, int i) {

    }

    @Override
    public float getExhaustion() {
        return 0F;
    }

    @Override
    public void setExhaustion(final float f) {
    }

    @Override
    public float getSaturation() {
        return 0F;
    }

    @Override
    public void setSaturation(final float f) {
    }

    @Override
    public int getFoodLevel() {
        return 0;
    }

    @Override
    public void setFoodLevel(final int i) {
    }

    @Override
    public int getSaturatedRegenRate() {
        return 0;
    }

    @Override
    public void setSaturatedRegenRate(int i) {

    }

    @Override
    public int getUnsaturatedRegenRate() {
        return 0;
    }

    @Override
    public void setUnsaturatedRegenRate(int i) {

    }

    @Override
    public int getStarvationRate() {
        return 0;
    }

    @Override
    public void setStarvationRate(int i) {

    }

    @Override
    public @Nullable Location getLastDeathLocation() {
        return null;
    }

    @Override
    public void setLastDeathLocation(@Nullable Location location) {

    }

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    public void setSprinting(final boolean bln) {
    }

    @Override
    public String getPlayerListName() {
        return name;
    }

    @Override
    public void setPlayerListName(final String name) {
    }

    @Override
    public @Nullable String getPlayerListHeader() {
        return null;
    }

    @Override
    public @Nullable String getPlayerListFooter() {
        return null;
    }

    @Override
    public void setPlayerListHeader(@Nullable String s) {

    }

    @Override
    public void setPlayerListFooter(@Nullable String s) {

    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {

    }

    @Override
    public int getTicksLived() {
        return 0;
    }

    @Override
    public void setTicksLived(final int i) {
    }

    @Override
    public double getMaxHealth() {
        return 0D;
    }

    @Override
    public void setMaxHealth(final double i) {
    }

    @Override
    public void giveExp(final int i) {
    }

    @Override
    public void giveExp(int i, boolean b) {

    }

    @Override
    public int applyMending(int i) {
        return 0;
    }

    @Override
    public float getExp() {
        return 0F;
    }

    @Override
    public void setExp(final float f) {
    }

    @Override
    public boolean teleport(final Location lctn, final TeleportCause tc) {
        return false;
    }

    @Override
    public boolean teleport(final Entity entity, final TeleportCause tc) {
        return false;
    }

    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public void setKiller(@Nullable Player player) {

    }

    @Override
    public void sendPluginMessage(final Plugin plugin, final String string, final byte[] bytes) {
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return Collections.emptySet();
    }

    @Override
    public boolean getAllowFlight() {
        return allowFlight;
    }

    @Override
    public void setAllowFlight(final boolean bln) {
        allowFlight = bln;
    }

    @Override
    public void setBedSpawnLocation(final Location lctn, final boolean force) {
    }

    @Override
    public void playEffect(final EntityEffect ee) {
    }

    @Override
    public void hidePlayer(final Player player) {
    }

    @Override
    public void hidePlayer(final Plugin plugin, final Player player) {

    }

    @Override
    public void showPlayer(final Player player) {
    }

    @Override
    public void showPlayer(final Plugin plugin, final Player player) {

    }

    @Override
    public boolean canSee(final Player player) {
        return false;
    }

    @Override
    public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {

    }

    @Override
    public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {

    }

    @Override
    public boolean canSee(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean addPotionEffect(final PotionEffect pe) {
        return false;
    }

    @Override
    public boolean addPotionEffect(final PotionEffect pe, final boolean bln) {
        return false;
    }

    @Override
    public boolean addPotionEffects(final Collection<PotionEffect> clctn) {
        return false;
    }

    @Override
    public boolean hasPotionEffect(final PotionEffectType pet) {
        return false;
    }

    @Override
    public PotionEffect getPotionEffect(final PotionEffectType type) {
        return null;
    }

    @Override
    public void removePotionEffect(final PotionEffectType pet) {
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return Collections.emptyList();
    }

    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> arg0) {
        return null;
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public <T> void playEffect(final Location lctn, final Effect effect, final T t) {
    }

    @Override
    public boolean breakBlock(@NotNull Block block) {
        return false;
    }

    @Override
    public boolean setWindowProperty(final Property prprt, final int i) {
        return false;
    }

    @Override
    public InventoryView getOpenInventory() {
        return null;
    }

    @Override
    public InventoryView openInventory(final Inventory invntr) {
        return null;
    }

    @Override
    public InventoryView openWorkbench(final Location lctn, final boolean bln) {
        return null;
    }

    @Override
    public InventoryView openEnchanting(final Location lctn, final boolean bln) {
        return null;
    }

    @Override
    public void openInventory(final InventoryView iv) {
    }

    @Override
    public InventoryView openMerchant(final Villager trader, final boolean force) {
        return null;
    }

    @Override
    public InventoryView openMerchant(final Merchant merchant, final boolean force) {
        return null;
    }

    @Override
    public @Nullable InventoryView openAnvil(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public @Nullable InventoryView openCartographyTable(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public @Nullable InventoryView openGrindstone(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public @Nullable InventoryView openLoom(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public @Nullable InventoryView openSmithingTable(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public @Nullable InventoryView openStonecutter(@Nullable Location location, boolean b) {
        return null;
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public void closeInventory(InventoryCloseEvent.@NotNull Reason reason) {

    }

    @Override
    public ItemStack getItemOnCursor() {
        return null;
    }

    @Override
    public void setItemOnCursor(final ItemStack is) {
    }

    @Override
    public boolean hasCooldown(final Material material) {
        return false;
    }

    @Override
    public int getCooldown(final Material material) {
        return 0;
    }

    @Override
    public void setCooldown(final Material material, final int ticks) {

    }

    @Override
    public boolean isDeeplySleeping() {
        return false;
    }

    @Override
    public void setMetadata(final String string, final MetadataValue mv) {
    }

    @Override
    public List<MetadataValue> getMetadata(final String string) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasMetadata(final String string) {
        return false;
    }

    @Override
    public void removeMetadata(final String string, final Plugin plugin) {
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(final String string) {
    }

    @Override
    public boolean beginConversation(final Conversation c) {
        return false;
    }

    @Override
    public void abandonConversation(final Conversation c) {
    }

    @Override
    public void sendMessage(final String[] strings) {
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {

    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {

    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isHandRaised() {
        return false;
    }

    @Override
    public @NotNull EquipmentSlot getHandRaised() {
        return null;
    }

    @Override
    public boolean isJumping() {
        return false;
    }

    @Override
    public void setJumping(boolean b) {

    }

    @Override
    public void playPickupItemAnimation(@NotNull Item item, int i) {

    }

    @Override
    public float getHurtDirection() {
        return 0;
    }

    @Override
    public void setHurtDirection(float v) {

    }

    @Override
    public @Nullable ItemStack getItemInUse() {
        return null;
    }

    @Override
    public void abandonConversation(final Conversation arg0, final ConversationAbandonedEvent arg1) {
    }

    @Override
    public boolean isFlying() {
        return isFlying;
    }

    @Override
    public void setFlying(final boolean arg0) {
        isFlying = arg0;
    }

    @Override
    public int getExpToLevel() {
        return 0;
    }

    @Override
    public @Nullable Entity releaseLeftShoulderEntity() {
        return null;
    }

    @Override
    public @Nullable Entity releaseRightShoulderEntity() {
        return null;
    }

    @Override
    public float getAttackCooldown() {
        return 0;
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return false;
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return 0;
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return false;
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return 0;
    }

    @Override
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey namespacedKey) {
        return false;
    }

    @Override
    public @NotNull Set<NamespacedKey> getDiscoveredRecipes() {
        return null;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        return null;
    }

    @Override
    public void setShoulderEntityLeft(final Entity entity) {

    }

    @Override
    public Entity getShoulderEntityRight() {
        return null;
    }

    @Override
    public void setShoulderEntityRight(final Entity entity) {

    }

    @Override
    public boolean hasLineOfSight(final Entity entity) {
        return false;
    }

    @Override
    public boolean hasLineOfSight(@NotNull Location location) {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public float getFlySpeed() {
        return 0.1f;
    }

    @Override
    public void setFlySpeed(final float value) throws IllegalArgumentException {
    }

    @Override
    public float getWalkSpeed() {
        return 0.2f;
    }

    @Override
    public void setWalkSpeed(final float value) throws IllegalArgumentException {
    }

    @Override
    public Inventory getEnderChest() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return null;
    }

    @Override
    public void playSound(final Location arg0, final Sound arg1, final float arg2, final float arg3) {
    }

    @Override
    public void giveExpLevels(final int i) {
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(final boolean bln) {
    }

    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public void setCanPickupItems(final boolean bln) {
    }

    @Override
    public Location getLocation(final Location lctn) {
        return lctn;
    }

    @Override
    public void setTexturePack(final String string) {
    }

    @Override
    public void setResourcePack(final String s) {
    }

    @Override
    public void setResourcePack(final String url, final byte[] hash) {

    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1) {

    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, boolean b) {

    }

    @Override
    public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1, boolean b) {

    }

    @Override
    public void setResourcePack(@NotNull String s, byte @Nullable [] bytes, @Nullable Component component, boolean b) {

    }

    @Override
    public void resetMaxHealth() {
    }

    @Override
    public @Nullable Component customName() {
        return null;
    }

    @Override
    public void customName(@Nullable Component component) {

    }

    @Override
    public String getCustomName() {
        return null;
    }

    @Override
    public void setCustomName(final String string) {
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void setCustomNameVisible(final boolean bln) {
    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public void setGlowing(final boolean flag) {

    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public void setInvulnerable(final boolean flag) {

    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void setSilent(final boolean flag) {

    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void setGravity(final boolean gravity) {

    }

    @Override
    public int getPortalCooldown() {
        return 0;
    }

    @Override
    public void setPortalCooldown(final int cooldown) {

    }

    @Override
    public Set<String> getScoreboardTags() {
        return null;
    }

    @Override
    public boolean addScoreboardTag(final String tag) {
        return false;
    }

    @Override
    public boolean removeScoreboardTag(final String tag) {
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    @Override
    public WeatherType getPlayerWeather() {
        return null; // per player weather, null means default anyways
    }

    @Override
    public void setPlayerWeather(final WeatherType arg0) {
    }

    @Override
    public void resetPlayerWeather() {
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public void setScoreboard(final Scoreboard scrbrd) throws IllegalArgumentException, IllegalStateException {
    }

    @Override
    public @Nullable WorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public void setWorldBorder(@Nullable WorldBorder worldBorder) {

    }

    @Override
    public void playSound(final Location arg0, final String arg1, final float arg2, final float arg3) {
    }

    @Override
    public void playSound(final Location location, final Sound sound, final SoundCategory category, final float volume, final float pitch) {

    }

    @Override
    public void playSound(final Location location, final String sound, final SoundCategory category, final float volume, final float pitch) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {

    }

    @Override
    public void stopSound(final Sound sound) {

    }

    @Override
    public void stopSound(final String sound) {

    }

    @Override
    public void stopSound(final Sound sound, final SoundCategory category) {

    }

    @Override
    public void stopSound(final String sound, final SoundCategory category) {

    }

    @Override
    public void stopSound(@NotNull SoundCategory soundCategory) {

    }

    @Override
    public void stopAllSounds() {

    }

    @Override
    public boolean isHealthScaled() {
        return false;
    }

    @Override
    public void setHealthScaled(final boolean arg0) {
    }

    @Override
    public double getHealthScale() {
        return 0D;
    }

    @Override
    public void sendHealthUpdate(double v, int i, float v1) {

    }

    @Override
    public void sendHealthUpdate() {

    }

    @Override
    public void setHealthScale(final double arg0) throws IllegalArgumentException {

    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder(final Entity arg0) {
        return false;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setGliding(final boolean gliding) {

    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public void setSwimming(boolean b) {

    }

    @Override
    public boolean isRiptiding() {
        return false;
    }

    @Override
    public void setAI(final boolean ai) {

    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void attack(@NotNull Entity entity) {

    }

    @Override
    public void swingMainHand() {

    }

    @Override
    public void swingOffHand() {

    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public @NotNull Set<UUID> getCollidableExemptions() {
        return null;
    }

    @Override
    public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return null;
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {

    }

    @Override
    public @NotNull EntityCategory getCategory() {
        return null;
    }

    @Override
    public void setInvisible(boolean b) {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public int getArrowsStuck() {
        return 0;
    }

    @Override
    public void setArrowsStuck(int i) {

    }

    @Override
    public int getShieldBlockingDelay() {
        return 0;
    }

    @Override
    public void setShieldBlockingDelay(int i) {

    }

    @Override
    public @NotNull ItemStack getActiveItem() {
        return null;
    }

    @Override
    public void clearActiveItem() {

    }

    @Override
    public int getItemUseRemainingTime() {
        return 0;
    }

    @Override
    public int getHandRaisedTime() {
        return 0;
    }

    @Override
    public void setCollidable(final boolean collidable) {

    }

    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> type, final Vector vector) {
        return null;
    }

    @Override
    public void sendSignChange(final Location arg0, final String[] arg1) throws IllegalArgumentException {
    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor) throws IllegalArgumentException {

    }

    @Override
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {

    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public long getLastLogin() {
        return 0;
    }

    @Override
    public long getLastSeen() {
        return 0;
    }

    @Override
    public void setBedSpawnLocation(final Location lctn) {
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = base.getName();
        if (this.name == null) {
            this.name = name;
        }
    }

    @Override
    public UUID getUniqueId() {
        return base.getUniqueId();
    }

    @Override
    public boolean isOp() {
        return base.isOp();
    }

    @Override
    public void setOp(final boolean value) {
        base.setOp(value);
    }

    @Override
    public boolean isOnline() {
        return base.isOnline();
    }

    @Override
    public boolean isBanned() {
        if (base.getName() == null && getName() != null) {
            return server.getBanList(BanList.Type.NAME).isBanned(getName());
        }
        return base.isBanned();
    }

    // Removed in 1.12, retain for backwards compatibility.
    @Deprecated
    public void setBanned(final boolean banned) {
        if (ReflUtil.getNmsVersionObject().isHigherThanOrEqualTo(ReflUtil.V1_12_R1)) {
            throw new UnsupportedOperationException("Cannot call setBanned on MC 1.12 and higher.");
        }
        if (base.getName() == null && getName() != null) {
            if (banned) {
                server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
            } else {
                server.getBanList(BanList.Type.NAME).pardon(getName());
            }
        }
        try {
            final Method method = base.getClass().getDeclaredMethod("setBanned", boolean.class);
            method.invoke(base, banned);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // This will never happen in a normal CraftBukkit pre-1.12 instance
            e.printStackTrace();
        }
    }

    @Override
    public boolean isWhitelisted() {
        return base.isWhitelisted();
    }

    @Override
    public void setWhitelisted(final boolean value) {
        base.setWhitelisted(value);
    }

    @Override
    public Player getPlayer() {
        return base.getPlayer();
    }

    @Override
    public long getFirstPlayed() {
        return base.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return base.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return base.hasPlayedBefore();
    }

    @Override
    public Map<String, Object> serialize() {
        return base.serialize();
    }

    @Override
    public Entity getSpectatorTarget() {
        return null;
    }

    @Override
    public void setSpectatorTarget(final Entity entity) {
    }

    @Override
    public void resetTitle() {
    }

    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count) {

    }

    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final T data) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final T data) {

    }

    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ) {

    }

    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final T data) {

    }

    @Override
    public void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {

    }

    @Override
    public void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final Location location, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data) {

    }

    @Override
    public <T> void spawnParticle(final Particle particle, final double x, final double y, final double z, final int count, final double offsetX, final double offsetY, final double offsetZ, final double extra, final T data) {

    }

    @Override
    public AdvancementProgress getAdvancementProgress(final Advancement advancement) {
        return null;
    }

    @Override
    public int getClientViewDistance() {
        return 0;
    }

    @Override
    public @NotNull Locale locale() {
        return null;
    }

    @Override
    public int getPing() {
        return 0;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public boolean getAffectsSpawning() {
        return false;
    }

    @Override
    public void setAffectsSpawning(boolean b) {

    }

    @Override
    public int getViewDistance() {
        return 0;
    }

    @Override
    public void setViewDistance(int i) {

    }

    @Override
    public int getSimulationDistance() {
        return 0;
    }

    @Override
    public void setSimulationDistance(int i) {

    }

    @Override
    public int getNoTickViewDistance() {
        return 0;
    }

    @Override
    public void setNoTickViewDistance(int i) {

    }

    @Override
    public int getSendViewDistance() {
        return 0;
    }

    @Override
    public void setSendViewDistance(int i) {

    }

    @Override
    public void updateCommands() {

    }

    @Override
    public void openBook(@NotNull ItemStack itemStack) {

    }

    @Override
    public void openSign(@NotNull Sign sign) {

    }

    @Override
    public boolean dropItem(boolean b) {
        return false;
    }

    @Override
    public void showDemoScreen() {

    }

    @Override
    public boolean isAllowingServerListings() {
        return false;
    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull String s1) {

    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b) {

    }

    @Override
    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b, @Nullable Component component) {

    }

    @Override
    public PlayerResourcePackStatusEvent.@Nullable Status getResourcePackStatus() {
        return null;
    }

    @Override
    public @Nullable String getResourcePackHash() {
        return null;
    }

    @Override
    public boolean hasResourcePack() {
        return false;
    }

    @Override
    public @NotNull PlayerProfile getPlayerProfile() {
        return null;
    }

    @Override
    public void setPlayerProfile(@NotNull PlayerProfile playerProfile) {

    }

    @Override
    public float getCooldownPeriod() {
        return 0;
    }

    @Override
    public float getCooledAttackStrength(float v) {
        return 0;
    }

    @Override
    public void resetCooldown() {

    }

    @Override
    public <T> @NotNull T getClientOption(@NotNull ClientOption<T> clientOption) {
        return null;
    }

    @Override
    public @Nullable Firework boostElytra(@NotNull ItemStack itemStack) {
        return null;
    }

    @Override
    public void sendOpLevel(byte b) {

    }

    @Override
    public @Nullable String getClientBrandName() {
        return null;
    }

    @Override
    public void setRotation(float v, float v1) {

    }

    @Override
    public boolean teleport(@NotNull Location location, @NotNull TeleportCause teleportCause, boolean b, boolean b1) {
        return false;
    }

    @Override
    public boolean teleport(@NotNull Location location, @NotNull TeleportCause teleportCause, boolean b, boolean b1, @NotNull RelativeTeleportFlag @NotNull ... relativeTeleportFlags) {
        return false;
    }

    @Override
    public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {

    }

    @Override
    public void lookAt(@NotNull Entity entity, @NotNull LookAnchor lookAnchor, @NotNull LookAnchor lookAnchor1) {

    }

    @Override
    public @NotNull Spigot spigot() {
        return null;
    }

    @Override
    public @NotNull Component name() {
        return null;
    }

    @Override
    public @NotNull Component teamDisplayName() {
        return null;
    }

    @Override
    public @Nullable Location getOrigin() {
        return null;
    }

    @Override
    public boolean fromMobSpawner() {
        return false;
    }

    @Override
    public CreatureSpawnEvent.@NotNull SpawnReason getEntitySpawnReason() {
        return null;
    }

    @Override
    public boolean isInRain() {
        return false;
    }

    @Override
    public boolean isInBubbleColumn() {
        return false;
    }

    @Override
    public boolean isInWaterOrRain() {
        return false;
    }

    @Override
    public boolean isInWaterOrBubbleColumn() {
        return false;
    }

    @Override
    public boolean isInWaterOrRainOrBubbleColumn() {
        return false;
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public boolean isTicking() {
        return false;
    }

    @Override
    public @NotNull Set<Player> getTrackedPlayers() {
        return null;
    }

    @Override
    public boolean spawnAt(@NotNull Location location, CreatureSpawnEvent.@NotNull SpawnReason spawnReason) {
        return false;
    }

    @Override
    public boolean isInPowderedSnow() {
        return false;
    }

    @Override
    public void sendTitle(final String title, final String subtitle) {

    }

    @Override
    public void sendTitle(final String title, final String subtitle, final int fadeIn, final int stay, final int fadeOut) {

    }

    @Override
    public AttributeInstance getAttribute(final Attribute attribute) {
        return null;
    }

    @Override
    public void registerAttribute(@NotNull Attribute attribute) {

    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}
