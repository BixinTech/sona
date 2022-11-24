package cn.bixin.sona.server.room.domain.db;

public class GroupUserCount {
    private long roomId;
    private long count;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
