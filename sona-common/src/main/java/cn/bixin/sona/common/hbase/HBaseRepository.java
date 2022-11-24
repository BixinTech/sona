package cn.bixin.sona.common.hbase;

import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.exception.YppRunTimeException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HBaseRepository {

    public static final String HBASE_READ_EXCEPTION_CODE = "8999";
    public static final String HBASE_DELETE_EXCEPTION_CODE = "8998";
    public static final String HBASE_INCREMENT_EXCEPTION_CODE = "8997";
    public static final String HBASE_PUT_EXCEPTION_CODE = "8996";
    public static final String HBASE_SCAN_EXCEPTION_CODE = "8995";
    public static final String HBASE_EXECUTE_EXCEPTION_CODE = "8994";

    private Connection connection;

    private HBaseRepository(Connection connection) {
        this.connection = connection;
    }

    public static HBaseRepository getInstance(Connection connection){
        return new HBaseRepository(connection);
    }

    public Connection getConnection(){
        return connection;
    }

    /**
     * 查询单个结果 get
     * @param tbName
     * @param rowKey
     * @return
     */
    public Result get(String tbName, byte[] rowKey) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            return table.get(new Get(rowKey));
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_READ_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 查询列表 gets
     *
     * @param tbName
     * @param gets
     * @return
     */
    public Result[] gets(String tbName, Collection<byte[]> gets) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            return table.get(gets.stream().map(Get::new).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_READ_EXCEPTION_CODE, e.getMessage()));
        }
    }

    public void delete(String tbName, byte[] rowKey) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            table.delete(new Delete(rowKey));
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_DELETE_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 批量删除
     *
     * @param tbName
     * @param rowKeys
     * @return
     */
    public void deletes(String tbName, Collection<byte[]> rowKeys) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            table.delete(rowKeys.stream().map(Delete::new).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_DELETE_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 自增
     * @param tbName
     * @param columnFamily
     * @param rowKey
     * @param field
     * @param amount
     * @return
     */
    public Long increment(String tbName, byte[] columnFamily, byte[] rowKey, String field, long amount) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            return table.incrementColumnValue(rowKey, columnFamily, Bytes.toBytes(field), amount);
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_INCREMENT_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 扫描
     *
     * @param tbName
     * @param scan
     * @param consumer
     */
    public void scan(String tbName, Scan scan, Consumer<Result> consumer) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName));
             ResultScanner resultScanner = table.getScanner(scan)) {
            for (Result result : resultScanner) {
                consumer.accept(result);
            }
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_SCAN_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 执行 consumer 操作
     * @param tbName
     * @param consumer
     */
    public void execute(String tbName, Consumer<Table> consumer) throws YppRunTimeException {
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            consumer.accept(table);
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_EXECUTE_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 保存
     * @param tbName
     * @param put
     * @return
     */
    public void put(String tbName, Put put) throws YppRunTimeException{
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            table.put(put);
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_PUT_EXCEPTION_CODE, e.getMessage()));
        }
    }

    /**
     * 批量保存
     * @param tbName
     * @param puts
     * @return
     */
    public void put(String tbName, List<Put> puts) throws YppRunTimeException{
        try (Table table = connection.getTable(TableName.valueOf(tbName))) {
            table.put(puts);
        } catch (IOException e) {
            throw new YppRunTimeException(Code.business(HBASE_PUT_EXCEPTION_CODE, e.getMessage()));
        }
    }
}