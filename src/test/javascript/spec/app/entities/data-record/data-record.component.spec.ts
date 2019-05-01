/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataRecordComponent } from 'app/entities/data-record/data-record.component';
import { DataRecordService } from 'app/entities/data-record/data-record.service';
import { DataRecord } from 'app/shared/model/data-record.model';

describe('Component Tests', () => {
    describe('DataRecord Management Component', () => {
        let comp: DataRecordComponent;
        let fixture: ComponentFixture<DataRecordComponent>;
        let service: DataRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataRecordComponent],
                providers: []
            })
                .overrideTemplate(DataRecordComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataRecordComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataRecordService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DataRecord(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.dataRecords[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
