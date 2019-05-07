/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { DataRecordDetailComponent } from 'app/entities/data-record/data-record-detail.component';
import { DataRecord } from 'app/shared/model/data-record.model';

describe('Component Tests', () => {
    describe('DataRecord Management Detail Component', () => {
        let comp: DataRecordDetailComponent;
        let fixture: ComponentFixture<DataRecordDetailComponent>;
        const route = ({ data: of({ dataRecord: new DataRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DataRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dataRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
