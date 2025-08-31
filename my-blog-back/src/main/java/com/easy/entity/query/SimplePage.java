package com.easy.entity.query;

import com.easy.enums.PageSize;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SimplePage {

     @Setter
     private Integer pageNo;       // 当前页（1-based）
     private Integer countTotal;   // 总记录数
     @Setter
     private Integer pageSize;     // 每页数量
     private Integer pageTotal;    // 总页数
     @Setter
     private Integer start;        // 当前页起始索引(0-based offset)
    /**
     * -- SETTER --
     *  设置本页 limit。注意：不会自动改 pageSize 或重新计算。
     *  如果你希望统一逻辑，请通过 setPageSize() 改。
     */
    @Setter
    private Integer end;          // 当前页取多少条（limit）；这里默认等于 pageSize

     /**
      * 无参构造：使用默认页号=1、无数据，总数=0、默认页大小
      */
     public SimplePage() {
         /** this(1, 0, PageSize.SIZE20.getSize()); */
     }

     /**
      * 常用构造：传页号、总条数、页大小。
      *
      * @param pageNo     页号（1-based；null或<1会自动设为1）
      * @param countTotal 总条数（<0时按0处理）
      * @param pageSize   每页条数（<=0使用默认）
      */
     public SimplePage(Integer pageNo, Integer countTotal, Integer pageSize) {
         if (null == pageNo) {
             pageNo = 0;
         }
         this.pageNo = pageNo;
         this.countTotal = countTotal;
         this.pageSize = pageSize;
         action();
     }

     /**
      * 偏移量构造：给定 offset + limit。
      * 注意：此构造不会自动计算 pageTotal，因为没有总条数信息。
      * 可用于仅需传 offset/limit 的场景（如部分 DAO）。
      */
     public SimplePage(Integer start, Integer end) {
         this.start = Math.max(start, 0);
         this.end = Math.max(end, 0);
       /*  this.pageSize = this.end;
         this.pageNo = 1;      // 无法精确推断；设为1
         this.countTotal = 0;  // 未知
         this.pageTotal = 0;   // 未知*/
     }

     /* ---------------- Core calc ---------------- */

     /**
      * 重新计算分页衍生字段。
      */
     private void action() {
         if (this.pageSize <= 0) {
             this.pageSize = PageSize.SIZE20.getSize();
         }
         if (this.countTotal > 0) {
             this.pageTotal = this.countTotal % this.pageSize == 0 ? this.countTotal / this.pageSize
                     : this.countTotal / this.pageSize + 1;
         } else {
             pageTotal = 1;
         }
         if (pageNo <= 1) {
             pageNo = 1;
         }
         if (pageNo > pageTotal) {
             pageNo = pageTotal;
         }

         // offset
         this.start = (pageNo - 1) * this.pageSize;

         // limit（想要“本页实际条数”可再调：Math.min(pageSize, countTotal-start)）
         this.end = this.pageSize;
     }

     /* ---------------- Getters / Setters ---------------- */

    /**
      * 通常不建议外部手动改 pageTotal；如果你一定要，可以提供此方法。
      */
     public void setPageTotal(Integer pageTotal) {
         this.pageTotal = Math.max(pageTotal, 0);
     }

    public void setCountTotal(Integer countTotal) {
         this.countTotal = countTotal;
         this.action();
     }

}
